package com.worldgether.mbal.model;


import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "Session")
@Entity
@Data
public class Sessions {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private User user;

    private Timestamp connectTime;

}
