package com.worldgether.mbal.model;

public enum Response {

    OK ("OK"),
    USER_ID_DOESNT_EXIST ("This user doesn't exist !"),
    MAIL_ALREADY_EXISTS ("This mail address already exists !"),
    ROLE_NOT_USER ("You're not allowed to perform this action !"),
    PASSWORD_NOT_CORRECT ("The password is not correct !");


    private String message = "";


    //Constructeur

    Response(String mess){

        this.message = mess;

    }

    public String toString(){

        return message;

    }

}
