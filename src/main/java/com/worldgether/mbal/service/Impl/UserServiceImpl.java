package com.worldgether.mbal.service.Impl;


import com.worldgether.mbal.model.*;
import com.worldgether.mbal.repository.*;
import com.worldgether.mbal.service.EmailService;
import com.worldgether.mbal.service.UserService;
import com.worldgether.mbal.service.storage.StorageService;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Evt_messageRepository evt_messageRepository;

    @Autowired
    private SessionsRepository sessionsRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private EmailService emailService;

    List<String> files = new ArrayList<String>();


    @Override
    public String connect(String username, String password) {

        if(username == null || password == null){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"LOGIN"));
            return null;
        }

        User user = this.getUser(username);

        if(user == null){
            log.error(LoggerMessage.getLog(LoggerMessage.USER_NOT_EXIST.toString(),"LOGIN",username));
            return Response.NOT_LOGGED_IN.toString();
        }

        if(!this.checkIfPasswordCorrect(username,password).equals(Response.OK.toString())){
            log.error(LoggerMessage.getLog(LoggerMessage.PASSWORD_NOT_CORRECT.toString(),"LOGIN",username));
            return Response.PASSWORD_NOT_CORRECT.toString();
        }

        Sessions sessionExistante = sessionsRepository.findByUser(user);

        if(sessionExistante != null){
            log.info(LoggerMessage.getLog(LoggerMessage.SESSION_ALREADY_EXIST.toString(),"LOGIN",username));
            return sessionExistante.getId();
        }

        Sessions newSession = new Sessions();

        newSession.setId(UUID.randomUUID().toString());

        newSession.setUser(user);

        newSession.setConnectTime(new Timestamp(new Date().getTime()));

        this.sessionsRepository.save(newSession);

        log.info(LoggerMessage.getLog(LoggerMessage.SESSION_CREATED.toString(),"LOGIN",newSession.getId(),username));

        return newSession.getId();

    }

    @Override
    public String logout(String session_id) {

        if(session_id == null){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"LOGOUT"));
            return null;
        }

        Sessions sessions = sessionsRepository.findById(session_id);

        if(sessions == null){
            log.error(LoggerMessage.getLog(LoggerMessage.SESSION_NOT_EXIST.toString(),"LOGOUT",session_id));
            return Response.SESSION_DOESNT_EXIST.toString();
        }

        sessionsRepository.delete(sessions);

        log.info(LoggerMessage.getLog(LoggerMessage.USER_LOGGEDOUT.toString(),"LOGOUT",sessions.getUser().getMail(),session_id));

        return Response.OK.toString();

    }

    @Override
    public String createUser(String nom, String prenom, String mail, String password, String numero_telephone, String role) {

        if(nom == null || prenom == null || mail == null || password == null || numero_telephone == null || role == null){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"CREATE"));
            return null;
        }

        User user = userRepository.findByMail(mail);

        if(user != null){
            log.error(LoggerMessage.getLog(LoggerMessage.USER_ALREADY_EXIST.toString(),"CREATE",mail));
            return Response.MAIL_ALREADY_EXISTS.toString();
        }

        User newUser = new User();

        newUser.setNom(nom);
        newUser.setPrenom(prenom);
        newUser.setMail(mail);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setCreation_date(new Timestamp(new Date().getTime()));
        newUser.setNumero_telephone(numero_telephone);
        newUser.setRoles(Arrays.asList(new Role(role)));
        newUser.setIsActivated(UUID.randomUUID().toString());

        Map<String, String> model = new HashMap<String, String>();
        model.put("name", newUser.getPrenom());
        model.put("id", newUser.getIsActivated());
        model.put("location", "France");
        model.put("signature", "http://mbal.serveurpi.ddns.net/");

        emailService.sendMail(newUser.getMail(),"Activation de votre compte MBAL",model,"email-template-createdAccount.ftl");

        userRepository.save(newUser);

        log.info(LoggerMessage.getLog(LoggerMessage.USER_CREATED.toString(),"CREATE",mail,newUser.getId().toString()));

        return Response.OK.toString();

    }

    @Override
    public String activateUser(String id_activation) {

        if(id_activation == null || id_activation.isEmpty()){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"ACTIVATEUSER"));
            return null;
        }

        User user = userRepository.findByIsActivated(id_activation);

        if(user == null){
            log.error(LoggerMessage.getLog(LoggerMessage.NO_USER_TO_ACTIVATE.toString(),"ACTIVATEUSER"));
            return Response.NO_SESSION_FOR_USER.toString();
        }

        user.setIsActivated("Activated");

        Map<String,String> model = new HashMap<>();
        model.put("location", "France");
        model.put("signature", "http://mbal.serveurpi.ddns.net/");

        emailService.sendMail(user.getMail(),"Compte activé sur MBAL",model,"email-template-activatedAccount.ftl");

        userRepository.save(user);

        return Response.OK.toString();

    }

    @Override
    public String getSessionIdByUsername(String username) {

        if(username == null || username.isEmpty()){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"GETSESSIONIDBYUSERNAME"));
            return null;
        }

        User user = userRepository.findByMail(username);

        if(user == null){
            log.error(LoggerMessage.getLog(LoggerMessage.USER_NOT_EXIST.toString(),"GETSESSIONIDBYUSERNAME",username));
            return Response.USER_ID_DOESNT_EXIST.toString();
        }

        Sessions session = sessionsRepository.findByUser(user);

        if(session == null){
            log.error(LoggerMessage.getLog(LoggerMessage.NO_SESSION_FOR_USER.toString(),"GETSESSIONIDBYUSERNAME",username));
            return Response.NO_SESSION_FOR_USER.toString();
        }

        return session.getId();

    }

    @Override
    public String setTokenPhoneForUser(String session_id, String token) {

        boolean isUser = false;

        if(session_id == null || token == null){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"SETTOKENPHONE"));
            return null;
        }

        Sessions session = sessionsRepository.findById(session_id);

        if(session == null){
            log.error(LoggerMessage.getLog(LoggerMessage.SESSION_NOT_EXIST.toString(),"SETTOKENPHONE",session_id));
            return Response.SESSION_DOESNT_EXIST.toString();
        }

        User user = session.getUser();

        if(user == null){
            log.error(LoggerMessage.getLog(LoggerMessage.NO_USER_FOR_SESSION.toString(),"SETTOKENPHONE",session_id));
            return Response.USER_ID_DOESNT_EXIST.toString();
        }

        for(Role role : user.getRoles()){
            if (role.getName().equals("USER")){
                isUser = true;
                break;
            }
        }

        if (!isUser){
            log.error(LoggerMessage.getLog(LoggerMessage.USER_WITHOUT_ROLE_USER.toString(),"SETTOKENPHONE",user.getMail()));
            return Response.ROLE_NOT_USER.toString();
        }

        user.setToken_telephone(token);

        userRepository.save(user);

        log.info(LoggerMessage.getLog(LoggerMessage.TOKEN_EDITED_FOR_USER.toString(),"SETTOKENPHONE",token,user.getMail()));

        return Response.OK.toString();

    }

    @Override
    public String deleteUser(String username) {

        if(username == null){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"DELETEUSER"));
            return null;
        }

        User user = userRepository.findByMail(username);

        if(user == null){
            log.error(LoggerMessage.getLog(LoggerMessage.USER_NOT_EXIST.toString(),"DELETEUSER",username));
            return Response.USER_ID_DOESNT_EXIST.toString();
        }

        if(user.getRoles() != null){
            List<Role> roles = user.getRoles();
            roleRepository.delete(roles);
        }

        Sessions session = sessionsRepository.findByUser(user);

        if(session != null){
            sessionsRepository.delete(session);
        }

        userRepository.delete(user);

        log.info(LoggerMessage.getLog(LoggerMessage.USER_DELETED.toString(),"DELETEUSER",username));

        return Response.OK.toString();

    }

    @Override
    public String updateUser(String nom, String prenom, String session_id, String password, String numero_telephone) {

        if(session_id == null){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"UPDATEUSER"));
            return null;
        }

        Sessions session = sessionsRepository.findById(session_id);

        if(session == null){
            log.error(LoggerMessage.getLog(LoggerMessage.SESSION_NOT_EXIST.toString(),"UPDATEUSER",session_id));
            return Response.SESSION_DOESNT_EXIST.toString();
        }

        User userToUpdate = session.getUser();

        if(userToUpdate == null){
            log.error(LoggerMessage.getLog(LoggerMessage.NO_USER_FOR_SESSION.toString(),"UPDATEUSER",session_id));
            return Response.USER_ID_DOESNT_EXIST.toString();
        }

        if(!nom.equals("")){
            userToUpdate.setNom(nom);
        }

        if(!prenom.equals("")){
            userToUpdate.setPrenom(prenom);
        }

        if(!password.equals("")){
            userToUpdate.setPassword(passwordEncoder.encode(password));
        }

        if(!numero_telephone.equals("")){
            userToUpdate.setNumero_telephone(numero_telephone);
        }

        userRepository.save(userToUpdate);

        log.info(LoggerMessage.getLog(LoggerMessage.USER_UPDATED.toString(),"UPDATEUSER",userToUpdate.getMail()));

        return Response.OK.toString();

    }

    @Override
    public String checkIfPasswordCorrect(String username, String password) {

        if(username == null || password == null){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"IFPASSWORDCORRECT"));
            return null;
        }

        User user = userRepository.findByMail(username);

        if(user == null){
            log.error(LoggerMessage.getLog(LoggerMessage.USER_NOT_EXIST.toString(),"IFPASSWORDCORRECT",username));
            return Response.USER_ID_DOESNT_EXIST.toString();
        }

        if(passwordEncoder.matches(password,user.getPassword())){
            log.info(LoggerMessage.getLog(LoggerMessage.PASSWORD_USER_CORRECT.toString(),"IFPASSWORDCORRECT",user.getMail()));
            return Response.OK.toString();
        }else{
            log.info(LoggerMessage.getLog(LoggerMessage.PASSWORD_NOT_CORRECT.toString(),"IFPASSWORDCORRECT",username));
            return Response.PASSWORD_NOT_CORRECT.toString();
        }
    }

    @Override
    public User getUser(String username) {

        if(username == null){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"GETUSER"));
            return null;
        }

        User user = userRepository.findByMail(username);

        if(user == null){
            log.error(LoggerMessage.getLog(LoggerMessage.USER_NOT_EXIST.toString(),"GETUSER",username));
            return null;
        }

        return user;

    }

    @Override
    public String getProfilePicture(String session_id) {

        if(session_id == null){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"GETPROFILEPICTURE"));
            return null;
        }

        Sessions session = sessionsRepository.findById(session_id);

        if(session == null){
            log.error(LoggerMessage.getLog(LoggerMessage.SESSION_NOT_EXIST.toString(),"GETPROFILEPICTURE",session_id));
            return null;
        }

        User user = session.getUser();

        if(user == null){
            log.error(LoggerMessage.getLog(LoggerMessage.NO_USER_FOR_SESSION.toString(),"GETPROFILEPICTURE",session_id));
            return null;
        }

        String pathFile = user.getProfile_picture_path();

        if(pathFile == null || pathFile.isEmpty()){
            log.error(LoggerMessage.getLog(LoggerMessage.NO_PATHFILE_FOR_USER.toString(),"GETPROFILEPICTURE",user.getMail()));
            return null;
        }

        log.info(LoggerMessage.getLog(LoggerMessage.GOT_PROFILE_PICTURE.toString(),"GETPROFILEPICTURE",user.getMail(),pathFile));

        return pathFile;

    }

    @Override
    public String setProfilePicture(String session_id, MultipartFile file) {

        if (session_id == null || file == null){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"SETPROFILEPICTURE"));
            return null;
        }

        Sessions session = sessionsRepository.findById(session_id);

        if (session == null){
            log.error(LoggerMessage.getLog(LoggerMessage.SESSION_NOT_EXIST.toString(),"SETPROFILEPICTURE",session_id));
            return Response.SESSION_DOESNT_EXIST.toString();
        }

        User user = session.getUser();

        if (user == null){
            log.error(LoggerMessage.getLog(LoggerMessage.NO_USER_FOR_SESSION.toString(),"SETPROFILEPICTURE",session_id));
            return Response.USER_ID_DOESNT_EXIST.toString();
        }

        String filePath = user.getProfile_picture_path();

        if (filePath != null && !filePath.isEmpty()){
            try {
                storageService.deleteSpecificFile(filePath);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        try {
            storageService.store(file);
            user.setProfile_picture_path(file.getOriginalFilename());
        } catch (IOException e) {
            log.error(LoggerMessage.getLog(LoggerMessage.PROFILE_PICTURE_NOT_UPDATED.toString(),"SETPROFILEPICTURE",user.getMail()));
            log.error(e.getMessage());
            return Response.IMAGE_NOT_UPDATED.toString();
        }

        userRepository.save(user);

        return Response.OK.toString();

    }

    @Override
    public Resource getFile(String filename) {

        if(filename == null || filename.isEmpty()){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"GETFILE"));
            return null;
        }

        Resource file = null;
        try {
            file = storageService.loadFile(filename);
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            return null;
        }

        if(file == null){
            log.error(LoggerMessage.getLog(LoggerMessage.NO_IMAGE_FOR_FILENAME.toString(),"GETFILE",filename));
            return null;
        }

        log.info(LoggerMessage.getLog(LoggerMessage.GET_FILE.toString(),"GETFILE",filename));

        return file;

    }

    @Override
    public Iterable<User> getAllUsers() {

        return userRepository.findAll();

    }

}
