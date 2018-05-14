package com.worldgether.mbal.service.Impl;


import com.worldgether.mbal.exception.NotFoundException;
import com.worldgether.mbal.model.*;
import com.worldgether.mbal.repository.*;
import com.worldgether.mbal.service.EmailService;
import com.worldgether.mbal.service.SmsSender;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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


    @Override
    public String connect(String username, String password, Client client) {

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

        if(!user.getIsActivated().equals("Activated")){
            log.error(LoggerMessage.getLog(LoggerMessage.USER_NOT_ACTIVATED.toString(),"LOGIN",username));
            return Response.USER_NOT_ACTIVATED.toString();
        }

        List<Sessions> sessionExistanteForUser = sessionsRepository.findByUser(user);

        if(sessionExistanteForUser != null){
            for(Sessions session : sessionExistanteForUser){
                if(session.getClient() == client){
                    log.info(LoggerMessage.getLog(LoggerMessage.SESSION_ALREADY_EXIST_CLIENT.toString(),"LOGIN",username,client.toString()));
                    return session.getId();
                }
            }
        }

        Sessions newSession = new Sessions();

        newSession.setId(UUID.randomUUID().toString());

        newSession.setUser(user);

        newSession.setConnectTime(new Timestamp(new Date().getTime()));

        newSession.setClient(client);

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
        Timestamp timestamp = new Timestamp(new Date().getTime());

        newUser.setNom(nom);
        newUser.setPrenom(prenom);
        newUser.setMail(mail);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setCreation_date(timestamp);
        newUser.setNumero_telephone(numero_telephone);
        newUser.setRoles(Arrays.asList(new Role(role)));
        newUser.setIsActivated(UUID.randomUUID().toString());

        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        localDateTime = localDateTime.plusDays(3);
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm");

        Map<String, String> model = new HashMap<String, String>();
        model.put("name", newUser.getPrenom());
        model.put("id", newUser.getIsActivated());
        model.put("date", localDateTime.format(formatterDate));
        model.put("hour", localDateTime.format(formatterHour));
        model.put("location", "France");
        model.put("signature", "http://mbal.serveurpi.ddns.net/");

        emailService.sendMail(newUser.getMail(),"Activation de votre compte MBAL",model,"email-template-createdAccount.ftl");

        userRepository.save(newUser);

        log.info(LoggerMessage.getLog(LoggerMessage.USER_CREATED.toString(),"CREATE",mail,newUser.getId().toString()));

        return Response.OK.toString();

    }

    @Override
    public String createUserByMobileApp(String nom, String prenom, String mail, String password, String numero_telephone, String role) {

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
        Timestamp timestamp = new Timestamp(new Date().getTime());

        newUser.setNom(nom);
        newUser.setPrenom(prenom);
        newUser.setMail(mail);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setCreation_date(timestamp);
        newUser.setNumero_telephone(numero_telephone);
        newUser.setRoles(Arrays.asList(new Role(role)));

        Random rand = new Random();
        String code = String.valueOf(rand.nextInt(9999) + 1000);
        newUser.setIsActivated(code);

        SmsSender.sendSms(numero_telephone,prenom,code);

        log.info(LoggerMessage.getLog(LoggerMessage.SMS_SENT.toString(),"CREATE",mail,numero_telephone));

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
    public String activateUserByPhone(String id_activation) {

        if(id_activation == null || id_activation.isEmpty()){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"ACTIVATEUSER"));
            return null;
        }

        User user = userRepository.findByIsActivated(id_activation);

        if(user == null){
            log.error(LoggerMessage.getLog(LoggerMessage.CODE_SMS_NOT_CORRECT.toString(),"ACTIVATEUSER",id_activation));
            return Response.CODE_NOT_CORRECT.toString();
        }

        user.setIsActivated("Activated");

        userRepository.save(user);

        return Response.OK.toString();

    }

    @Override
    public String getSessionIdByUsername(String username, Client client) {

        if(username == null || username.isEmpty()){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"GETSESSIONIDBYUSERNAME"));
            return null;
        }

        User user = userRepository.findByMail(username);

        if(user == null){
            log.error(LoggerMessage.getLog(LoggerMessage.USER_NOT_EXIST.toString(),"GETSESSIONIDBYUSERNAME",username));
            return Response.USER_ID_DOESNT_EXIST.toString();
        }

        List<Sessions> sessions = sessionsRepository.findByUser(user);

        if(sessions != null){
            for(Sessions session : sessions){
                if(session.getClient() == client){
                    return session.getId();
                }
            }
        }

        log.error(LoggerMessage.getLog(LoggerMessage.NO_SESSION_FOR_USER.toString(),"GETSESSIONIDBYUSERNAME",username));
        return Response.NO_SESSION_FOR_USER.toString();

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

        List<Sessions> sessions = sessionsRepository.findByUser(user);

        if(sessions != null){
            sessionsRepository.delete(sessions);
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
    public Resource getFile(String filename) throws NotFoundException {

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
