**API**

**URL**

   * [http://serveurpi.ddns.net/MBAL/api](http://serveurpi.ddns.net/MBAL/api) - Début de la requête
   
**TO DO**
* Request : <_http://serveurpi.ddns.net/MBAL/oauth/token?grant_type=password&username=admin&password=Valentin34>
*  Response : `JSON -> access_token` TO add at each header of a request


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
  
  
**LOGOUT**
----

* **URL**

  <_/user/logout/{session_id}_>

* **Method:**
  
  `GET`

* **Data Params**

  **Required:**
   
     `session_id=[string]`
    
     
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{ Response : String }`
 
* **Error Response:**

  * **Code:** 500 INTERNAL_SERVER_ERROR
  

**CREATE USER**
----

* **URL**

  <_/user/create_>

* **Method:**
  
  `POST`

* **Data Params**

  **Required:**
   
     `name=[string]`
     `prenom=[string]`
     `mail=[string]`
     `password=[string]`
     `num_tel=[string]`
     `role=[string] -> USER`
    
     
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{ Response : String }`
 
* **Error Response:**

  * **Code:** 500 INTERNAL_SERVER_ERROR
  
  
**ACTIVATE USER**
----

* **URL**

  <_/user/activate/{id}_>

* **Method:**
  
  `GET`

* **Data Params**

  **Required:**
   
     `id=[string] -> present in mail sent`
    
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{ Response : String }`
 
* **Error Response:**

  * **Code:** 500 INTERNAL_SERVER_ERROR
  
**GET SESSION ID BY USER**
----

* **URL**

  <_/user/getSessionIdByUsername/{username}/_> 
  Le dernier / est important !

* **Method:**
  
  `GET`

* **Data Params**

  **Required:**
   
     `username=[string] -> mail address`
    
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{ Response : String }`
 
* **Error Response:**

  * **Code:** 500 INTERNAL_SERVER_ERROR
  
**DELETE USER**
----

* **URL**

  <_/user/deleteUser_> 

* **Method:**
  
  `POST`

* **Data Params**

  **Required:**
   
     `username=[string] -> mail address`
    
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{ Response : String }`
 
* **Error Response:**

  * **Code:** 500 INTERNAL_SERVER_ERROR
  
  
**UPDATE USER**
----

* **URL**

  <_/user/updateUser_> 

* **Method:**
  
  `POST`

* **Data Params**

  **Required (blank if attribute not to be updated) :**
   
     `session_id=[string] -> actual session`
     `name=[string]`
     `prenom=[string]`
     `password=[string]`
     `numero_telephone=[string]`
    
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{ Response : String }`
 
* **Error Response:**

  * **Code:** 500 INTERNAL_SERVER_ERROR
  
**CHECK IF PASSWORD CORRECT**
----

* **URL**

  <_/user/checkIfPasswordCorrect_> 

* **Method:**
  
  `POST`

* **Data Params**

  **Required :**
   
     `username=[string] -> mail address`
     `password=[string]`
    
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{ Response : String }`
 
* **Error Response:**

  * **Code:** 500 INTERNAL_SERVER_ERROR
  
**SET FAMILY OF USER**
----

* **URL**

  <_/user/setFamilyForUser_> 

* **Method:**
  
  `POST`

* **Data Params**

  **Required :**
   
     `session_id=[string] -> actual session id`
     `name_family=[string]`
     `password_family=[string]`
    
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{ Response : String }`
 
* **Error Response:**

  * **Code:** 500 INTERNAL_SERVER_ERROR
  
**GET USER BY MAIL**
----

* **URL**

  <_/getUserByName/{username}/_> 

* **Method:**
  
  `GET`

* **Data Params**

  **Required :**
   
     `username=[string]`
    
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `JSON -> attribute :
                        nom,prenom,mail,creation_date,numero_telephone,
                        token_telephone,nom_photo_profil,isActivated,
                        Family(name,creation_date)`
 
* **Error Response:**

  * **Code:** 500 INTERNAL_SERVER_ERROR
  
  
**GET USERS BY FAMILY ID**
----

* **URL**

  <_/user/getUsersByFamilyName/{family_name}_> 

* **Method:**
  
  `GET`

* **Data Params**

  **Required :**
   
     `family_name=[String]`
    
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{ JSON -> List of users }`
 
* **Error Response:**

  * **Code:** 500 INTERNAL_SERVER_ERROR
  
  
**GET PROFILE PICTURE PATH FOR USER**
----

* **URL**

  <_/user/getPathProfilePicture/{session_id}_> 

* **Method:**
  
  `GET`

* **Data Params**

  **Required :**
   
     `session_id=[string] -> actual session id`
    
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{ Response : String }`
 
* **Error Response:**

  * **Code:** 500 INTERNAL_SERVER_ERROR
  
**SET PROFILE PICTURE FOR USER**
----

* **URL**

  <_/user/setProfilePicture_> 

* **Method:**
  
  `POST`

* **Data Params**

  **Required :**
   
     `session_id=[string] -> actual session id`
     `uploadfile=[File] -> Multipart File`
    
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{ Response : String }`
 
* **Error Response:**

  * **Code:** 500 INTERNAL_SERVER_ERROR
  
**GET PICTURE**
----

* **URL**

  <_/user/files/{filename}_> 

* **Method:**
  
  `GET`

* **Data Params**

  **Required :**
   
     `filename=[String] -> name_profile_picture`
    
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** `{ Picture To Download }`
 
* **Error Response:**

  * **Code:** 500 INTERNAL_SERVER_ERROR