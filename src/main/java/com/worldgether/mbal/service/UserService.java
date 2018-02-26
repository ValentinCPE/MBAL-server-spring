package com.worldgether.mbal.service;


import com.worldgether.mbal.model.Role;
import com.worldgether.mbal.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    String connect(String username,String password);

    String createUser(String nom, String prenom, String mail, String password, String numero_telephone, String role);

    String setTokenPhoneForUser(String session_id, String token);

    String deleteUser(String username);

    String updateUser(String nom, String prenom, String session_id, String password, String numero_telephone);

    String checkIfPasswordCorrect(String username, String password);

    User getUser(String username);

    Iterable<User> getAllUsers();

}
