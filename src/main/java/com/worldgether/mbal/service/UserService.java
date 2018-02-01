package com.worldgether.mbal.service;


import com.worldgether.mbal.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    User createUser(String nom, String prenom, String mail, String password, String numero_telephone);

    User setFamilyForUser(Integer id_user, Integer id_family, String password_family);

    User setTokenPhoneForUser(Integer id_user, String token);

    String deleteUser(Integer id_user);

    User updateUser(Integer id_user, String nom, String prenom, String mail, String password, String numero_telephone);

    User getUser(Integer id_user);

    Iterable<User> getAllUsers();

}
