package com.worldgether.mbal.service;


import com.worldgether.mbal.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.util.List;

@Service
public interface UserService {

    String connect(String username,String password);

    String logout(String session_id);

    String createUser(String nom, String prenom, String mail, String password, String numero_telephone, String role);

    String createUserByMobileApp(String nom, String prenom, String mail, String password, String numero_telephone, String role);

    String activateUser(String id_activation);

    String activateUserByPhone(String id_activation);

    String getSessionIdByUsername(String username);

    String setTokenPhoneForUser(String session_id, String token);

    String deleteUser(String username);

    String updateUser(String nom, String prenom, String session_id, String password, String numero_telephone);

    String checkIfPasswordCorrect(String username, String password);

    User getUser(String username);

    String getProfilePicture(String session_id);

    String setProfilePicture(String session_id, MultipartFile file);

    Resource getFile(String filename);

    Iterable<User> getAllUsers();

}
