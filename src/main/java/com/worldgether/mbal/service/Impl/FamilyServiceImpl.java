package com.worldgether.mbal.service.Impl;

import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.model.User;
import com.worldgether.mbal.repository.FamilyRepository;
import com.worldgether.mbal.repository.UserRepository;
import com.worldgether.mbal.service.FamilyService;
import com.worldgether.mbal.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public Family createFamily(Integer user_id, String name, String password) {

        if(name == null || password == null){
            return null;
        }

        Family family = new Family();
        family.setName(name);
        family.setPassword(PasswordService.hashPassword(password));
        family.setCreation_date(new Timestamp(new Date().getTime()));

        User user = userRepository.findById(user_id);

        if(user == null){
            return null;
        }

        user.setFamily(family);

        familyRepository.save(family);
        userRepository.save(user);

        return family;
    }

    @Override
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

    }

    @Override
    public Family updatePasswordFamily(Integer id_family, String new_password) {

        if(id_family == null || new_password == null){
            return null;
        }

        Family family = familyRepository.findById(id_family);

        if(family == null){
            return null;
        }

        family.setPassword(PasswordService.hashPassword(new_password));

        familyRepository.save(family);

        return family;

    }

    @Override
    public String deleteFamily(Integer id_family) {

        if (id_family == null){
            return null;
        }

        Family family = familyRepository.findById(id_family);

        if (family == null){
            return null;
        }

        familyRepository.delete(family);

        return "deleted";

    }

    @Override
    public Family getFamily(Integer id_family) {

        if(id_family == null){
            return null;
        }

        Family family = familyRepository.findById(id_family);

        if(family == null){
            return null;
        }

        return family;

    }

    @Override
    public List<User> getUsersByFamily(Integer id_family) {

        if(id_family == null){
            return null;
        }

        Family family = familyRepository.findById(id_family);

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
    public String setFamilyForUser(Integer id_user, Integer id_family, String password_family) {

        if(id_user == null || id_family == null || password_family == null){
            return null;
        }

        Family family = familyRepository.findById(id_family);

        if(family == null){
            return null;
        }

        if(!family.getPassword().equals(PasswordService.hashPassword(password_family))){
            return "PASSWORD_INCORRECT";
        }

        User user = userRepository.findById(id_user);

        if(user == null){
            return null;
        }

        user.setFamily(family);

        userRepository.save(user);

        return "USER_ADDED_TO_FAMILY";
    }

}
