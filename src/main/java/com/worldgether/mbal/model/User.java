package com.worldgether.mbal.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "User")
@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String nom;

    private String prenom;

    private String mail;

    private String password;

    private Timestamp creation_date;

    private String numero_telephone;

    private String token_telephone;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;


}
