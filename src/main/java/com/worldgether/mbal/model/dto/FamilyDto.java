package com.worldgether.mbal.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamilyDto {

    private int id;

    private String name;

    private Timestamp creation_date;

}
