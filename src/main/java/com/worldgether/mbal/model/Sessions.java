package com.worldgether.mbal.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

@Document(collection = "sessions")
@Data
public class Sessions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Indexed
    private String id;

    private String uuid;

    private User user;

    private Family family;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date connectTime;

}
