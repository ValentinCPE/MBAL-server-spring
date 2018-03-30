package com.worldgether.mbal.service.Impl;

import com.worldgether.mbal.model.*;
import com.worldgether.mbal.repository.FamilyRepository;
import com.worldgether.mbal.repository.SessionsRepository;
import com.worldgether.mbal.repository.UserRepository;
import com.worldgether.mbal.service.FamilyService;
import com.worldgether.mbal.service.PasswordService;
import com.worldgether.mbal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class FamilyServiceImpl implements FamilyService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionsRepository sessionsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String createFamily(String session_id, String name, String password) {

        if(session_id == null || name == null || password == null){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"CREATEFAMILY"));
            return null;
        }

        Family familyExist = familyRepository.findByName(name);

        if(familyExist != null){
            log.error(LoggerMessage.getLog(LoggerMessage.FAMILY_ALREADY_EXIST.toString(),"CREATEFAMILY",name));
            return Response.FAMILY_ALREADY_EXISTS.toString();
        }

        Family family = new Family();
        family.setName(name);
        family.setPassword(passwordEncoder.encode(password));
        family.setCreation_date(new Timestamp(new Date().getTime()));

        Sessions session = sessionsRepository.findById(session_id);

        if(session == null){
            log.error(LoggerMessage.getLog(LoggerMessage.SESSION_NOT_EXIST.toString(),"CREATEFAMILY",session_id));
            return null;
        }

        User user = session.getUser();

        if(user == null){
            log.error(LoggerMessage.getLog(LoggerMessage.NO_USER_FOR_SESSION.toString(),"CREATEFAMILY",session_id));
            return Response.USER_ID_DOESNT_EXIST.toString();
        }

        user.setFamily(family);

        familyRepository.save(family);
        userRepository.save(user);

        log.info(LoggerMessage.getLog(LoggerMessage.FAMILY_CREATED.toString(),"CREATEFAMILY",family.getName(),family.getId().toString()));

        return Response.OK.toString();
    }

  /*  @Override
    public Family updateNameFamily(Integer id_family, String name) {
        
        if(id_family == null || name == null){
            return null;
        }

        Family family = familyRepository.findById(id_family);

        if(family == null){
            return null;
        }

        family.setName(name);

        familyRepository.save(family);

        return family;

    } */

    @Override
    public String updatePasswordFamily(String session_id, String new_password) {

        if(session_id == null || new_password == null){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"UPDATEPASSWORDFAMILY"));
            return null;
        }

        Sessions session = sessionsRepository.findById(session_id);

        if(session == null){
            log.error(LoggerMessage.getLog(LoggerMessage.SESSION_NOT_EXIST.toString(),"UPDATEPASSWORDFAMILY",session_id));
            return null;
        }

        Family family = session.getUser().getFamily();

        if(family == null){
            log.error(LoggerMessage.getLog(LoggerMessage.NO_FAMILY_FOR_SESSION.toString(),"UPDATEPASSWORDFAMILY",session_id));
            return Response.FAMILY_ID_DOESNT_EXIST.toString();
        }

    /*    if(!passwordEncoder.matches(old_password,family.getPassword())){
            return Response.PASSWORD_NOT_CORRECT.toString();
        } */

        family.setPassword(passwordEncoder.encode(new_password));

        familyRepository.save(family);

        log.info(LoggerMessage.getLog(LoggerMessage.PASSWORD_FAMILY_UPDATED.toString(),"UPDATEPASSWORDFAMILY",family.getName()));

        return Response.OK.toString();

    }

    @Override
    public String checkIfPasswordFamilyCorrect(String session_id, String password) {

        if(session_id == null || password == null){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"IFPASSWORDFAMILYCORRECT"));
            return null;
        }

        Sessions session = sessionsRepository.findById(session_id);

        if(session == null){
            log.error(LoggerMessage.getLog(LoggerMessage.SESSION_NOT_EXIST.toString(),"IFPASSWORDFAMILYCORRECT",session_id));
            return Response.SESSION_DOESNT_EXIST.toString();
        }

        Family family = session.getUser().getFamily();
        
        if(family == null){
            log.error(LoggerMessage.getLog(LoggerMessage.NO_FAMILY_FOR_SESSION.toString(),"IFPASSWORDFAMILYCORRECT",session_id));
            return Response.FAMILY_ID_DOESNT_EXIST.toString();
        }

        if(!passwordEncoder.matches(password,family.getPassword())){
            log.info(LoggerMessage.getLog(LoggerMessage.PASSWORD_FAMILY_NOT_CORRECT.toString(),"IFPASSWORDFAMILYCORRECT",family.getName()));
            return Response.PASSWORD_NOT_CORRECT.toString();
        }
        
        log.info(LoggerMessage.getLog(LoggerMessage.PASSWORD_FAMILY_CORRECT.toString(),"IFPASSWORDFAMILYCORRECT",family.getName()));

        return Response.OK.toString();

    }

    @Override
    public String deleteFamily(String name) {

        if (name == null){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"DELETEFAMILY"));
            return null;
        }

        Family family = familyRepository.findByName(name);

        if (family == null){
            log.error(LoggerMessage.getLog(LoggerMessage.FAMILY_NOT_EXIST.toString(),"DELETEFAMILY",name));
            return Response.FAMILY_ID_DOESNT_EXIST.toString();
        }

        List<User> users = getUsersByFamily(name);

        if(users != null){
            for(User user : users){
                userRepository.findByMail(user.getMail()).setFamily(null);
            }
        }

        familyRepository.delete(family);
        
        log.info(LoggerMessage.getLog(LoggerMessage.FAMILY_DELETED.toString(),"DELETEFAMILY",name));

        return Response.OK.toString();

    }

    @Override
    public Family getFamily(String name) {

        if(name == null){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"GETFAMILY"));
            return null;
        }

        Family family = familyRepository.findByName(name);

        if(family == null){
            log.error(LoggerMessage.getLog(LoggerMessage.FAMILY_NOT_EXIST.toString(),"GETFAMILY",name));
            return null;
        }

        return family;

    }

    @Override
    public List<User> getUsersByFamily(String name) {

        if(name == null){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"GETUSERSBYFAMILY"));
            return null;
        }

        Family family = familyRepository.findByName(name);

        if(family == null){
            log.error(LoggerMessage.getLog(LoggerMessage.FAMILY_NOT_EXIST.toString(),"GETUSERSBYFAMILY",name));
            return null;
        }

        List<User> users = userRepository.findUserByFamily(family);

        if (users == null){
            log.error(LoggerMessage.getLog(LoggerMessage.USERS_NOT_EXIST_IN_FAMILY.toString(),"GETUSERSBYFAMILY",name));
            return null;
        }

        return users;

    }

    @Override
    public String setFamilyForUser(String session_id, String name_family, String password_family) {

        if(session_id == null || name_family == null || password_family == null){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"SETFAMILYFORUSER"));
            return null;
        }

        Family family = familyRepository.findByName(name_family);

        if(family == null){
            log.error(LoggerMessage.getLog(LoggerMessage.FAMILY_NOT_EXIST.toString(),"SETFAMILYFORUSER",name_family));
            return Response.FAMILY_ID_DOESNT_EXIST.toString();
        }

        if(!passwordEncoder.matches(password_family,family.getPassword())){
            log.info(LoggerMessage.getLog(LoggerMessage.PASSWORD_FAMILY_NOT_CORRECT.toString(),"SETFAMILYFORUSER",name_family));
            return Response.PASSWORD_NOT_CORRECT.toString();
        }

        Sessions session = sessionsRepository.findById(session_id);

        if(session == null){
            log.error(LoggerMessage.getLog(LoggerMessage.SESSION_NOT_EXIST.toString(),"SETFAMILYFORUSER",session_id));
            return Response.SESSION_DOESNT_EXIST.toString();
        }

        User user = userRepository.findById(session.getUser().getId());

        if(user == null){
            log.error(LoggerMessage.getLog(LoggerMessage.NO_USER_FOR_SESSION.toString(),"SETFAMILYFORUSER",session_id));
            return Response.USER_ID_DOESNT_EXIST.toString();
        }

        user.setFamily(family);

        userRepository.save(user);

        log.info(LoggerMessage.getLog(LoggerMessage.FAMILY_FOR_USER_DEFINED.toString(),"SETFAMILYFORUSER",user.getMail(),family.getName()));

        return Response.OK.toString();
    }

    @Override
    public List<String> getPathProfilePictureForFamily(String name) {

        if(name == null || name.isEmpty()){
            log.error(LoggerMessage.getLog(LoggerMessage.PARAMETER_NULL.toString(),"GETPATHPROFILEPICTUREFAMILY"));
            return null;
        }

        Family family = familyRepository.findByName(name);

        if(family == null){
            log.error(LoggerMessage.getLog(LoggerMessage.FAMILY_NOT_EXIST.toString(),"GETPATHPROFILEPICTUREFAMILY",name));
            return new ArrayList<>(Arrays.asList(Response.FAMILY_ID_DOESNT_EXIST.toString()));
        }

        List<User> users = userRepository.findUserByFamily(family);

        if(users == null){
            return new ArrayList<>(Arrays.asList(Response.NO_USER_FOR_FAMILY.toString()));
        }

        List<String> result = new ArrayList<>();
        for(User user : users){
            result.add(user.getProfile_picture_path());
            log.info(LoggerMessage.getLog(LoggerMessage.IMAGE_SUCCESSFULLY_UPDATED.toString(),"GETPATHPROFILEPICTUREFAMILY",user.getProfile_picture_path(),user.getMail()));
        }

        return result;

    }

}
