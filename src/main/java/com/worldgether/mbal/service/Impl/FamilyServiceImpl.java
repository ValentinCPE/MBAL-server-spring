package com.worldgether.mbal.service.Impl;

import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.model.Response;
import com.worldgether.mbal.model.User;
import com.worldgether.mbal.repository.FamilyRepository;
import com.worldgether.mbal.repository.UserRepository;
import com.worldgether.mbal.service.FamilyService;
import com.worldgether.mbal.service.PasswordService;
import com.worldgether.mbal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class FamilyServiceImpl implements FamilyService {

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String createFamily(String username, String name, String password) {

        if(username == null || name == null || password == null){
            return null;
        }

        if(familyRepository.findByName(name) != null){
            return Response.FAMILY_ALREADY_EXISTS.toString();
        }

        Family family = new Family();
        family.setName(name);
        family.setPassword(passwordEncoder.encode(password));
        family.setCreation_date(new Timestamp(new Date().getTime()));

        User user = userRepository.findByMail(username);

        if(user == null){
            return Response.USER_ID_DOESNT_EXIST.toString();
        }

        user.setFamily(family);

        familyRepository.save(family);
        userRepository.save(user);

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
    public String updatePasswordFamily(String name, String old_password, String new_password) {

        if(name == null || old_password == null || new_password == null){
            return null;
        }

        Family family = familyRepository.findByName(name);

        if(family == null){
            return Response.FAMILY_ID_DOESNT_EXIST.toString();
        }

        if(!passwordEncoder.matches(old_password,family.getPassword())){
            return Response.PASSWORD_NOT_CORRECT.toString();
        }

        family.setPassword(passwordEncoder.encode(new_password));

        familyRepository.save(family);

        return Response.OK.toString();

    }

    @Override
    public String deleteFamily(String name) {

        if (name == null){
            return null;
        }

        Family family = familyRepository.findByName(name);

        if (family == null){
            return Response.FAMILY_ID_DOESNT_EXIST.toString();
        }

        List<User> users = getUsersByFamily(name);

        if(users != null){
            for(User user : users){
                userRepository.findByMail(user.getMail()).setFamily(null);
            }
        }

        familyRepository.delete(family);

        return Response.DELETION_SUCCESSFUL.toString();

    }

    @Override
    public Family getFamily(String name) {

        if(name == null){
            return null;
        }

        Family family = familyRepository.findByName(name);

        if(family == null){
            return null;
        }

        return family;

    }

    @Override
    public List<User> getUsersByFamily(String name) {

        if(name == null){
            return null;
        }

        Family family = familyRepository.findByName(name);

        if(family == null){
            return null;
        }

        List<User> users = userRepository.findUserByFamily(family);

        if (users == null){
            return null;
        }

        return users;

    }

    @Override
    public String setFamilyForUser(String username, String name_family, String password_family) {

        if(username == null || name_family == null || password_family == null){
            return null;
        }

        Family family = familyRepository.findByName(name_family);

        if(family == null){
            return Response.FAMILY_ID_DOESNT_EXIST.toString();
        }

        if(!passwordEncoder.matches(password_family,family.getPassword())){
            return Response.PASSWORD_NOT_CORRECT.toString();
        }

        User user = userRepository.findByMail(username);

        if(user == null){
            return Response.USER_ID_DOESNT_EXIST.toString();
        }

        user.setFamily(family);

        userRepository.save(user);

        return Response.USER_ADDED_TO_FAMILY.toString();
    }

}
