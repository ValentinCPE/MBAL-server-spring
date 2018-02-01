package com.worldgether.mbal.repository;

import com.worldgether.mbal.model.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Long> {

    User findById(int id_user);

}
