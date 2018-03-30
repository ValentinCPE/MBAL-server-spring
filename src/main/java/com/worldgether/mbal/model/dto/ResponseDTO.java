package com.worldgether.mbal.model.dto;

import lombok.Data;

@Data
public class ResponseDTO {

    private String response;

    public ResponseDTO(String response){
        this.response = response;
    }

}
