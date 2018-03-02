package com.worldgether.mbal.model;

public enum LoggerMessage {

     PARAMETER_NULL ("[%s] Certain(s) paramètre(s) ont une valeur nulle"),
     USER_NOT_EXIST("[%s] User %s n'existe pas en BDD"),
     PASSWORD_NOT_CORRECT ("[%s] Le password du user %s n'est pas correct"),
     SESSION_ALREADY_EXIST ("[%s] Une session existe déjà pour le user %s"),
     SESSION_CREATED ("[%s] Session d'ID %s créée pour le user %s"),
     SESSION_NOT_EXIST ("[%s] Session d'ID %s n'existe pas"),
     USER_LOGGEDOUT ("[%s] Le user %s avec l'ID de Session %s a été déconnecté"),
     USER_DELETED ("[%s] Le user %s a été supprimé"),
     USER_ALREADY_EXIST ("[%s] User %s existe déjà"),
     USER_CREATED ("[%s] User %s d'ID %s a bien été créé"),
     NO_USER_FOR_SESSION ("[%s] Il n'y a pas de User pour la Session d'ID %s"),
     USER_WITHOUT_ROLE_USER ("[%s] Le User %s doit avoir le rôle User"),
     TOKEN_EDITED_FOR_USER ("[%s] Token %s modifié pour le User %s"),
     USER_UPDATED ("[%s] User %s modifié avec succès"),
     PASSWORD_USER_CORRECT ("[%s] Le password du user %s est correct"),
     FAMILY_ALREADY_EXIST ("[%s] Family %s existe déjà"),
     FAMILY_CREATED ("[%s] Family %s d'ID %s a bien été créé"),
     NO_FAMILY_FOR_SESSION ("[%s] Il n'y a pas de Family pour la Session d'ID %s"),
     PASSWORD_FAMILY_UPDATED ("[%s] Le password de la Family %s a été modifié"),
     PASSWORD_FAMILY_NOT_CORRECT ("[%s] Le password de la Family %s n'est pas correct"),
     PASSWORD_FAMILY_CORRECT ("[%s] Le password de la Family %s est correct"),
     FAMILY_NOT_EXIST("[%s] Family %s n'existe pas en BDD"),
     FAMILY_DELETED("[%s] Family %s a été supprimée"),
     USERS_NOT_EXIST_IN_FAMILY ("[%s] La liste Users est nulle dans la Family %s"),
     FAMILY_FOR_USER_DEFINED ("[%s] User %s a désormais comme Family %s"),
     IMAGE_SUCCESSFULLY_UPDATED ("[%s] L'image %s a bien été téléchargée pour le User %s"),
     IMAGE_SIZE_PROBLEM ("[%s] User %s a surement essayé de charger une image supèrieure à %s"),
     NO_PATHFILE_FOR_USER ("[%s] User %s n'a pas d'image de profil"),
     NO_IMAGE_FOR_FILENAME ("[%s] Aucune image pour le nom de fichier %s");


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
