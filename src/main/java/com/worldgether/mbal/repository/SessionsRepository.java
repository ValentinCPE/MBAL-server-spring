package com.worldgether.mbal.repository;

import com.worldgether.mbal.model.Sessions;
import com.worldgether.mbal.model.User;
import org.springframework.data.repository.CrudRepository;

public interface SessionsRepository extends CrudRepository<Sessions,String> {

    Sessions findById(String id);

    Sessions findByUser(User user);

}
