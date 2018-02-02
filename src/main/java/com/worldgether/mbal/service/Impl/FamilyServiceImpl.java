package com.worldgether.mbal.service.Impl;

import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.model.User;
import com.worldgether.mbal.repository.FamilyRepository;
import com.worldgether.mbal.repository.UserRepository;
import com.worldgether.mbal.service.FamilyService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        family.setPassword(password);
        family.setCreation_date(new Timestamp(new Date().getTime()));

        User user = userRepository.findById(user_id);

        if(user == null){
            return null;
        }

        user.setFamily(family);

        List<User> users = new ArrayList<>();
        users.add(user);

        family.setUsers(users);

        familyRepository.save(family);
        userRepository.save(user);

        return family;
    }

    @Override
    public Family updateNameFamily(String id_family, String name) {
        return null;
    }

    @Override
    public Family updatePasswordFamily(String id_family, String new_password) {
        return null;
    }

    @Override
    public String deleteFamily(String id_family) {
        return null;
    }

    @Override
    public List<User> getUsersByFamily(String id_family) {
        return null;
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

        if(!family.getPassword().equals(password_family)){
            return "PASSWORD_INCORRECT";
        }

        User user = userRepository.findById(id_user);

        if(user == null){
            return null;
        }

        user.setFamily(family);

        List<User> users = family.getUsers();

        users.add(user);

        familyRepository.save(family);
        userRepository.save(user);

        return "USER_ADDED_TO_FAMILY";
    }

}
