package com.worldgether.mbal.repository;

import com.worldgether.mbal.model.Evt_message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Evt_messageRepository extends MongoRepository<Evt_message, String> {
}
