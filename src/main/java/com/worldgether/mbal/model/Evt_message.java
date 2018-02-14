package com.worldgether.mbal.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

@Document(collection = "evt_messages")
@Data
public class Evt_message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Indexed
    private String id;

    @Enumerated(EnumType.STRING)
    private Message message;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date dateUTC;

    private User user;

    private Family family;

}
