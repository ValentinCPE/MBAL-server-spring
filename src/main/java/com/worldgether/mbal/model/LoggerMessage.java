package com.worldgether.mbal.model;

public enum LoggerMessage {

     PARAMETER_NULL ("[%s] Certain(s) parametre(s) ont une valeur nulle"),
     USER_NOT_EXIST("[%s] User %s n'existe pas en BDD"),
     PASSWORD_NOT_CORRECT ("[%s] Le password du user %s n'est pas correct"),
     SESSION_ALREADY_EXIST ("[%s] Une session existe dejà pour le user %s"),
     SESSION_ALREADY_EXIST_CLIENT ("[%s] Une session existe dejà pour le user %s sur le client %s"),
     SESSION_CREATED ("[%s] Session d'ID %s creee pour le user %s"),
     SESSION_NOT_EXIST ("[%s] Session d'ID %s n'existe pas"),
     USER_LOGGEDOUT ("[%s] Le user %s avec l'ID de Session %s a ete deconnecte"),
     USER_DELETED ("[%s] Le user %s a ete supprime"),
     USER_ALREADY_EXIST ("[%s] User %s existe dejà"),
     USER_CREATED ("[%s] User %s d'ID %s a bien ete cree"),
     NO_USER_FOR_SESSION ("[%s] Il n'y a pas de User pour la Session d'ID %s"),
     USER_WITHOUT_ROLE_USER ("[%s] Le User %s doit avoir le rôle User"),
     TOKEN_EDITED_FOR_USER ("[%s] Token %s modifie pour le User %s"),
     USER_UPDATED ("[%s] User %s modifie avec succes"),
     PASSWORD_USER_CORRECT ("[%s] Le password du user %s est correct"),
     FAMILY_ALREADY_EXIST ("[%s] Family %s existe dejà"),
     FAMILY_CREATED ("[%s] Family %s d'ID %s a bien ete cree"),
     NO_FAMILY_FOR_SESSION ("[%s] Il n'y a pas de Family pour la Session d'ID %s"),
     PASSWORD_FAMILY_UPDATED ("[%s] Le password de la Family %s a ete modifie"),
     PASSWORD_FAMILY_NOT_CORRECT ("[%s] Le password de la Family %s n'est pas correct"),
     PASSWORD_FAMILY_CORRECT ("[%s] Le password de la Family %s est correct"),
     FAMILY_NOT_EXIST("[%s] Family %s n'existe pas en BDD"),
     FAMILY_DELETED("[%s] Family %s a ete supprimee"),
     USERS_NOT_EXIST_IN_FAMILY ("[%s] La liste Users est nulle dans la Family %s"),
     FAMILY_FOR_USER_DEFINED ("[%s] User %s a desormais comme Family %s"),
     IMAGE_SUCCESSFULLY_UPDATED ("[%s] L'image %s a bien ete telechargee pour le User %s"),
     IMAGE_SIZE_PROBLEM ("[%s] User %s a surement essaye de charger une image superieure à %s"),
     NO_PATHFILE_FOR_USER ("[%s] User %s n'a pas d'image de profil"),
     NO_IMAGE_FOR_FILENAME ("[%s] Aucune image pour le nom de fichier %s"),
     PROFILE_PICTURE_NOT_UPDATED ("[%s] La nouvelle photo de profil du User %s n'a pas ete ajoutee apres que l'ancienne ait ete supprimee"),
     NO_SESSION_FOR_USER ("[%s] Il n'y a pas de session pour le User %s"),
     GOT_PROFILE_PICTURE ("[%s] Le User %s a recupere sa photo de profil %s"),
     GET_FILE ("[%s] Fichier %s telecharge"),
     NO_USER_TO_ACTIVATE ("[%s] Aucun User avec ce code d'activation"),
     USER_NOT_ACTIVATED ("[%s] Le User %s ne peut pas se connecter car il n'a pas active son compte"),
     SMS_SENT ("[%s] Sms has been sent to %s (%s)"),
     CODE_SMS_NOT_CORRECT ("[%s] Code of user %s is not correct"),
     EVENTS_NOT_EXIST ("[%s] Cette famille n'a pas d'evenement");

    private String message = "";


    //Constructeur

    LoggerMessage(String mess){

        this.message = mess;

    }

    public static String getLog(String log, String... parameters){

        return String.format(log,parameters);

    }

    public String toString(){

        return message;

    }

}
