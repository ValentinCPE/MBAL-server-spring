package com.worldgether.mbal.service.Impl;


import com.worldgether.mbal.model.Response;
import com.worldgether.mbal.model.Role;
import com.worldgether.mbal.model.User;
import com.worldgether.mbal.repository.FamilyRepository;
import com.worldgether.mbal.repository.RoleRepository;
import com.worldgether.mbal.repository.UserRepository;
import com.worldgether.mbal.service.PasswordService;
import com.worldgether.mbal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public String createUser(String nom, String prenom, String mail, String password, String numero_telephone, String role) {

        if(nom == null || prenom == null || mail == null || password == null || numero_telephone == null || role == null){
            return null;
        }

        if(userRepository.findByMail(mail) != null){
            return Response.MAIL_ALREADY_EXISTS.toString();
        }

        User newUser = new User();

        newUser.setNom(nom);
        newUser.setPrenom(prenom);
        newUser.setMail(mail);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setCreation_date(new Timestamp(new Date().getTime()));
        newUser.setNumero_telephone(numero_telephone);

        Role roleExists = roleRepository.findByName(role);

        if(roleExists == null){
            newUser.setRoles(Arrays.asList(new Role(role)));
        }else{
            newUser.setRoles(Arrays.asList(roleExists));
        }

        userRepository.save(newUser);

        return Response.OK.toString();

    }

    @Override
    public String setTokenPhoneForUser(String username, String token) {

        boolean isUser = false;

        if(username == null || token == null){
            return null;
        }

        User user = userRepository.findByMail(username);

        if(user == null){
            return Response.USER_ID_DOESNT_EXIST.toString();
        }

        for(Role role : user.getRoles()){
            if (role.getName().equals("USER")){
                isUser = true;
                break;
            }
        }

        if (!isUser){
            return Response.ROLE_NOT_USER.toString();
        }

        user.setToken_telephone(token);

        userRepository.save(user);

        return Response.OK.toString();

    }

    @Override
    public String deleteUser(String username) {

        if(username == null){
            return null;
        }

        User user = userRepository.findByMail(username);

        if(user == null){
            return Response.USER_ID_DOESNT_EXIST.toString();
        }

        userRepository.delete(user);

        return Response.DELETION_SUCCESSFUL.toString();

    }

    @Override
    public String updateUser(String nom, String prenom, String mail, String password, String numero_telephone) {

        if(mail == null){
            return null;
        }

        User userToUpdate = userRepository.findByMail(mail);

        if(userToUpdate == null){
            return Response.USER_ID_DOESNT_EXIST.toString();
        }

        if(nom != ""){
            userToUpdate.setNom(nom);
        }

        if(prenom != ""){
            userToUpdate.setPrenom(prenom);
        }

        if(mail != ""){
            userToUpdate.setMail(mail);
        }

        if(password != ""){
            userToUpdate.setPassword(passwordEncoder.encode(password));
        }

        if(numero_telephone != ""){
            userToUpdate.setNumero_telephone(numero_telephone);
        }

        userRepository.save(userToUpdate);

        return Response.OK.toString();

    }

    @Override
    public String checkIfPasswordCorrect(String username, String password) {

        if(username == null || password == null){
            return null;
        }

        User user = userRepository.findByMail(username);

        if(user == null){
            return Response.USER_ID_DOESNT_EXIST.toString();
        }

        if(passwordEncoder.matches(password,user.getPassword())){
            return Response.OK.toString();
        }else{
            return Response.PASSWORD_NOT_CORRECT.toString();
        }
    }

    @Override
    public User getUser(String username) {

        if(username == null){
            return null;
        }

        User user = userRepository.findByMail(username);

        if(user == null){
            return null;
        }

        return user;

    }

    @Override
    public Iterable<User> getAllUsers() {

        return userRepository.findAll();

    }

}
