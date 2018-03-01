#API

##URL

   * [http://serveurpi.ddns.net/MBAL/api](http://serveurpi.ddns.net/MBAL/api) - Début de la requête
   
###Methodes

**LOGIN**
```
 /user/login
``` 
  _Post Param_ : username - password
  
  _Return_ : Session_ID
  
  _Error_ : INTERNAL_SERVER_ERROR
     
  
**LOGOUT**

```
 **/user/logout/{session_id}**
  
  _Path Get Param_ : session_id
  
  _Return_ : "OK"
  _Error_ :  INTERNAL_SERVER_ERROR
  
```
   
   
   
**LOGIN**
----

* **URL**

  <_/user/login_>

* **Method:**
  
  `POST`

* **Data Params**

  **Required:**
   
     `username=[string]`
    
     `password=[string]`
     
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{ session_id : UYHJGF-457686-HKJKH }`
 
* **Error Response:**

  * **Code:** 500 INTERNAL_SERVER_ERROR