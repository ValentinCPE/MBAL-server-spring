package com.worldgether.mbal.repository;

import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UserRepository extends CrudRepository<User, Long> {

    User findById(int id_user);

    List<User> findUserByFamily(Family family);

}
