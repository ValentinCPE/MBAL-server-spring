package com.worldgether.mbal.repository;

import com.worldgether.mbal.model.Sessions;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionsRepository extends MongoRepository<Sessions, String> {

    Sessions findByUuid(String uuid);

}
