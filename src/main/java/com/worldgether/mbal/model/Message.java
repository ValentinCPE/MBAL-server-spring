package com.worldgether.mbal.model;

public enum Message {

    NEW_COURRIEL ("Nouveau(x) courriel(s) recu(s) a "),
    COURRIEL_FETCHED ("Courriel(s) recupere(s) a "),
    UPDATE_AVAILABLE ("Une mise a jour est disponible depuis ");


    private String message = "";


    //Constructeur

    Message(String mess){

        this.message = mess;

    }

    public String toString(){

        return message;

    }

}
