package com.worldgether.mbal.model;

public enum Response {

    OK ("OK"),
    USER_ID_DOESNT_EXIST ("This user doesn't exist !"),
    FAMILY_ID_DOESNT_EXIST ("This family doesn't exist !"),
    MAIL_ALREADY_EXISTS ("This mail address already exists !"),
    ROLE_NOT_USER ("You're not allowed to perform this action !"),
    PASSWORD_NOT_CORRECT ("The password is not correct !"),
    DELETION_SUCCESSFUL ("Deleted !"),
    USER_ADDED_TO_FAMILY ("The family has been added to this user !"),
    FAMILY_ALREADY_EXISTS ("This family already exists !"),
    NOT_LOGGED_IN ("Not logged in !"),
    SESSION_DOESNT_EXIST ("The session doesn't exist !");


    private String message = "";


    //Constructeur

    Response(String mess){

        this.message = mess;

    }

    public String toString(){

        return message;

    }

}
