package com.worldgether.mbal.service;

import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FamilyService {

    String createFamily(String session_id, String name, String password);

    String updatePasswordFamily(String session_id, String new_password);

    String checkIfPasswordFamilyCorrect(String session_id, String password);

    String deleteFamily(String name);

    Family getFamily(String name);

    List<User> getUsersByFamily(String name);

    String setFamilyForUser(String session_id, String name_family, String password_family);

}
