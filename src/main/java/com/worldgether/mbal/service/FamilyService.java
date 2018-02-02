package com.worldgether.mbal.service;

import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FamilyService {

    Family createFamily(Integer user_id, String name, String password);

    Family updateNameFamily(String id_family, String name);

    Family updatePasswordFamily(String id_family, String new_password);

    String deleteFamily(String id_family);

    List<User> getUsersByFamily(String id_family);

    String setFamilyForUser(Integer id_user, Integer id_family, String password_family);

}
