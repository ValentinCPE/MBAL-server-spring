package com.worldgether.mbal.service.Impl;


import com.worldgether.mbal.model.*;
import com.worldgether.mbal.repository.*;
import com.worldgether.mbal.service.PasswordService;
import com.worldgether.mbal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
            log.error(LoggerMessage.getLog(LoggerMessage.SESSION_ALREADY_EXIST.toString(),"LOGIN",username));
            return Response.SESSION_ALREADY_EXISTS.toString();
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

        userRepository.save(newUser);

        log.info(LoggerMessage.getLog(LoggerMessage.USER_CREATED.toString(),"CREATE",mail,newUser.getId().toString()));

        return Response.OK.toString();

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
    public Iterable<User> getAllUsers() {

        return userRepository.findAll();

    }

}
