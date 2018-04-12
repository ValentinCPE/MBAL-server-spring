package com.worldgether.mbal.repository;

import com.worldgether.mbal.model.Evt_message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface Evt_messageRepository extends MongoRepository<Evt_message, String> {

    @Query(value = "{ 'family.name' : ?0 }", fields = "{ 'message' : 1, 'timestamp' : 1 }")
    List<Evt_message> findByFamily_Name(String family_name);

}
