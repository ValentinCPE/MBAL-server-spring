package com.worldgether.mbal.service;

import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.model.User;
import com.worldgether.mbal.model.dto.FamilyDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FamilyService {

    String createFamily(String session_id, String name, String password);

    String updatePasswordFamily(String session_id, String new_password);

    String checkIfPasswordFamilyCorrect(String name, String password);

    String deleteFamily(String name);

    Family getFamily(String name);

    List<FamilyDto> getAllFamilies();

    List<User> getUsersByFamily(String name);

    String setFamilyForUser(String username, String name_family, String password_family);

    List<String> getPathProfilePictureForFamily(String name);

}
