package com.worldgether.mbal.service;


import com.worldgether.mbal.model.Role;
import com.worldgether.mbal.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    String createUser(String nom, String prenom, String mail, String password, String numero_telephone, String role);

    String setTokenPhoneForUser(Integer id_user, String token);

    String deleteUser(Integer id_user);

    String updateUser(Integer id_user, String nom, String prenom, String mail, String password, String numero_telephone);

    String checkIfPasswordCorrect(Integer id_user, String password);

    User getUser(Integer id_user);

    Iterable<User> getAllUsers();

}
