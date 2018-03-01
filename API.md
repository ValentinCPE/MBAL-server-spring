#API

##URL

   * [http://serveurpi.ddns.net/MBAL/api](http://serveurpi.ddns.net/MBAL/api) - Début de la requête
   
###Methodes

```
 **/user/login**
  
  _Post Param_ : username - password
  
  _Return_ : Session_ID
  _Error_ : INTERNAL_SERVER_ERROR
     
  
```

```
 **/user/logout/{session_id}**
  
  _Path Get Param_ : session_id
  
  _Return_ : "OK"
  _Error_ :  INTERNAL_SERVER_ERROR
  
```
   
    