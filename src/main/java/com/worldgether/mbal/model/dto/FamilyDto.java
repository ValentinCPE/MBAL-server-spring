package com.worldgether.mbal.model.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class FamilyDto {

    private String name;

    private Timestamp creation_date;

    public FamilyDto(String name, Timestamp creation_date){
        this.name = name;
        this.creation_date = creation_date;
    }

}
