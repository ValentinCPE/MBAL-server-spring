package com.worldgether.mbal.model;

import com.mongodb.DBObject;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Document(collection = "evt_messages")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Evt_message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Indexed
    private String id;

    @Enumerated(EnumType.STRING)
    private Message message;

    private Long timestamp;

    private DBObject date;

    private User user;

    private Family family;

}
