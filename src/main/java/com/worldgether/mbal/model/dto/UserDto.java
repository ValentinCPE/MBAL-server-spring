package com.worldgether.mbal.model.dto;

import com.worldgether.mbal.model.User;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {

    private String nom;

    private String prenom;

    private String mail;

    private Timestamp creation_date;

    private String numero_telephone;

    private String token_telephone;

    private FamilyDto family;

    public UserDto(String nom, String prenom, String mail, Timestamp creation_date, String numero_telephone,
                   String token_telephone, FamilyDto family){
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.creation_date = creation_date;
        this.numero_telephone = numero_telephone;
        this.token_telephone = token_telephone;
        this.family = family;
    }

    public static List<UserDto> getUsersDtoByList(List<User> users){

        List<UserDto> usersDto = new ArrayList<>();

        for(User user : users){
            usersDto.add(new UserDto(user.getNom(),user.getPrenom(),user.getMail(),
                    user.getCreation_date(), user.getNumero_telephone(), user.getToken_telephone(),
                    new FamilyDto(user.getFamily().getName(),user.getFamily().getCreation_date())));
        }
        return usersDto;
    }

}
