package com.worldgether.mbal.service;

import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FamilyService {

    Family createFamily(Integer user_id, String name, String password);

    Family updateNameFamily(Integer id_family, String name);

    Family updatePasswordFamily(Integer id_family, String new_password);

    String deleteFamily(Integer id_family);

    Family getFamily(Integer id_family);

    List<User> getUsersByFamily(Integer id_family);

    String setFamilyForUser(Integer id_user, Integer id_family, String password_family);

}
