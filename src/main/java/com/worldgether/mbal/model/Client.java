package com.worldgether.mbal.model;

public enum Client {

    WEB ("WEB"),
    ANDROID ("ANDROID");


    private String client = "";


    //Constructeur

    Client(String cli){

        this.client = cli;

    }

    public String toString(){

        return client;

    }

}
