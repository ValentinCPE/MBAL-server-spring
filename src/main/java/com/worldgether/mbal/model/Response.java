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
    SESSION_DOESNT_EXIST ("This session doesn't exist !"),
    SESSION_ALREADY_EXISTS ("A session already exists for this user !"),
    IMAGE_NOT_UPDATED ("L'image n'a pas été modifiée"),
    NO_SESSION_FOR_USER ("Vous n'avez pas de session en cours"),
    NO_USER_TO_ACTIVATE ("Aucun User avec ce code d'activation"),
    USER_NOT_ACTIVATED ("Vous n'avez pas activé votre compte !"),
    CODE_NOT_CORRECT ("Le code que vous avez saisi n'est pas correct !"),
    NO_USER_FOR_FAMILY ("Pas d'utilisateur pour cette famille !");


    private String message = "";


    //Constructeur

    Response(String mess){

        this.message = mess;

    }

    public String toString(){

        return message;

    }

}
