package com.worldgether.mbal.repository;

import com.worldgether.mbal.model.Evt_message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface Evt_messageRepository extends MongoRepository<Evt_message, String> {

    @Query(value = "{ 'family.name' : ?0 }", fields = "{ 'message' : 1, 'timestamp' : 1 }")
    List<Evt_message> findByFamily_Name(String family_name);

    @Query(fields = "{ 'message' : 1, 'timestamp' : 1, 'user.nom' : 1, 'user.prenom' : 1, 'user.mail' : 1, 'user.numero_telephone' : 1," +
            "'user.profile_picture_path' : 1 }")
    Evt_message findTopByOrderByTimestampDesc();

    @Query(fields = "{ 'message' : 1, 'timestamp' : 1, 'user.nom' : 1, 'user.prenom' : 1, 'user.mail' : 1, 'user.numero_telephone' : 1," +
            "'user.profile_picture_path' : 1 }")
    List<Evt_message> findByFamily_NameAndTimestampBetween(String family_name, Long start, Long end);

}
