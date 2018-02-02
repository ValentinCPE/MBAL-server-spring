package com.worldgether.mbal.service.Impl;

import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.model.User;
import com.worldgether.mbal.repository.FamilyRepository;
import com.worldgether.mbal.service.FamilyService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FamilyServiceImpl implements FamilyService {

    @Autowired
    private FamilyRepository familyRepository;

    @Override
    public Family createFamily(String name, String password) {
        return null;
    }

    @Override
    public Family updateNameFamily(String id_family, String name) {
        return null;
    }

    @Override
    public Family updatePasswordFamily(String id_family, String new_password) {
        return null;
    }

    @Override
    public String deleteFamily(String id_family) {
        return null;
    }

    @Override
    public List<User> getUsersByFamily(String id_family) {
        return null;
    }

}
