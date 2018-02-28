package com.worldgether.mbal.model;

public enum Message {

    NEW_COURRIEL ("Nouveau(x) courriel(s) recu(s)"),
    COURRIEL_FETCHED ("Courriel(s) recupere(s)"),
    UPDATE_AVAILABLE ("Une mise a jour est disponible"),
    NOTIFICATION_FAMILY_SENT ("La famille a été modifiée");


    private String message = "";


    //Constructeur

    Message(String mess){

        this.message = mess;

    }

    public String toString(){

        return message;

    }

}
