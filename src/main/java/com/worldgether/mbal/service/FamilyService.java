package com.worldgether.mbal.service;

import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FamilyService {

    String createFamily(String username, String name, String password);

    String updatePasswordFamily(String name, String old_password, String new_password);

    String deleteFamily(String name);

    Family getFamily(String name);

    List<User> getUsersByFamily(String name);

    String setFamilyForUser(String username, String name_family, String password_family);

}
