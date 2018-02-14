package com.worldgether.mbal.service.Impl;


import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.model.User;
import com.worldgether.mbal.repository.FamilyRepository;
import com.worldgether.mbal.repository.UserRepository;
import com.worldgether.mbal.service.PasswordService;
import com.worldgether.mbal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FamilyRepository familyRepository;

    @Override
    public User createUser(String nom, String prenom, String mail, String password, String numero_telephone) {

        if(nom == null || prenom == null || mail == null || password == null || numero_telephone == null){
            return null;
        }

        User newUser = new User();

        newUser.setNom(nom);
        newUser.setPrenom(prenom);
        newUser.setMail(mail);
        newUser.setPassword(PasswordService.hashPassword(password));
        newUser.setCreation_date(new Timestamp(new Date().getTime()));
        newUser.setNumero_telephone(numero_telephone);

        userRepository.save(newUser);

        return newUser;

    }

    @Override
    public User setTokenPhoneForUser(Integer id_user, String token) {

        if(id_user == null || token == null){
            return null;
        }

        User user = userRepository.findById(id_user);

        if(user == null){
            return null;
        }

        user.setToken_telephone(token);

        userRepository.save(user);

        return user;

    }

    @Override
    public String deleteUser(Integer id_user) {

        if(id_user == null){
            return null;
        }

        User user = userRepository.findById(id_user);

        if(user == null){
            return null;
        }

        userRepository.delete(user);

        return "deleted";

    }

    @Override
    public User updateUser(Integer id_user, String nom, String prenom, String mail, String password, String numero_telephone) {

        if(id_user == null){
            return null;
        }

        User userToUpdate = userRepository.findById(id_user);

        if(userToUpdate == null){
            return null;
        }

        if(nom != null){
            userToUpdate.setNom(nom);
        }

        if(prenom != null){
            userToUpdate.setPrenom(prenom);
        }

        if(mail != null){
            userToUpdate.setMail(mail);
        }

        if(password != null){
            userToUpdate.setPassword(PasswordService.hashPassword(password));
        }

        if(numero_telephone != null){
            userToUpdate.setNumero_telephone(numero_telephone);
        }

        userRepository.save(userToUpdate);

        return userToUpdate;

    }

    @Override
    public Boolean checkIfPasswordCorrect(Integer id_user, String password) {

        if(id_user == null || password == null){
            return null;
        }

        User user = userRepository.findById(id_user);

        if(user == null){
            return null;
        }

        if(user.getPassword().equals(PasswordService.hashPassword(password))){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public User getUser(Integer id_user) {

        if(id_user == null){
            return null;
        }

        User user = userRepository.findById(id_user);

        if(user == null){
            return null;
        }

        return user;

    }

    @Override
    public Iterable<User> getAllUsers() {

        return userRepository.findAll();

    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
