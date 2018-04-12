package com.worldgether.mbal.repository;

import com.worldgether.mbal.model.Sessions;
import com.worldgether.mbal.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SessionsRepository extends CrudRepository<Sessions,String> {

    Sessions findById(String id);

    List<Sessions> findByUser(User user);

}
