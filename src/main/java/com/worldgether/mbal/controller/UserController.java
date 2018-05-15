package com.worldgether.mbal.controller;

import com.worldgether.mbal.model.Client;
import com.worldgether.mbal.model.Response;
import com.worldgether.mbal.model.Sessions;
import com.worldgether.mbal.model.User;
import com.worldgether.mbal.model.dto.FamilyDto;
import com.worldgether.mbal.model.dto.ResponseDTO;
import com.worldgether.mbal.model.dto.UserDto;
import com.worldgether.mbal.repository.SessionsRepository;
import com.worldgether.mbal.service.FamilyService;
import com.worldgether.mbal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Controller
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FamilyService familyService;

    @Autowired
    private SessionsRepository sessionsRepository;

    @MessageMapping("/infosChanged/{session_id}/{family_id}")
    @SendTo("/alert/infosChanged/{family_id}")
    public ResponseEntity<String> alertFamilyChanged(@DestinationVariable String session_id,
                                     @DestinationVariable String family_id) throws Exception {

        if(session_id == null) return new ResponseEntity<>(Response.SESSION_DOESNT_EXIST.toString(),HttpStatus.INTERNAL_SERVER_ERROR);

        Sessions session = sessionsRepository.findById(session_id);

        if(session == null) return new ResponseEntity<>(Response.SESSION_DOESNT_EXIST.toString(),HttpStatus.INTERNAL_SERVER_ERROR);

        User user = session.getUser();

        if(user == null) return new ResponseEntity<>(Response.SESSION_DOESNT_EXIST.toString(),HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>("Les informations concernant votre famille viennent d'être modifiées par " + user.getPrenom(),HttpStatus.OK);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<ResponseDTO> connect(@RequestParam("username") String username,
                                               @RequestParam("password") String password,
                                               @RequestParam("client") Client client){

        String sessionId = userService.connect(username,password,client);

        if(sessionId == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseDTO(sessionId),HttpStatus.OK);

    }

    @RequestMapping(value = "/logout/{session_id}", method = RequestMethod.GET, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<ResponseDTO> logout(@PathVariable("session_id") String session_id){

        String response = userService.logout(session_id);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseDTO(response),HttpStatus.OK);

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<ResponseDTO> createUser(@RequestParam("name") String name,
                                           @RequestParam("prenom") String prenom,
                                           @RequestParam("mail") String mail,
                                           @RequestParam("password") String password,
                                           @RequestParam("num_tel") String numero_telephone,
                                           @RequestParam("role") String role) {

        String response = userService.createUser(name,prenom,mail,password,numero_telephone,role);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseDTO(response),HttpStatus.OK);

    }

    @RequestMapping(value = "/createByMobileApp", method = RequestMethod.POST)
    public ResponseEntity<String> createUserByMobileApp(@RequestParam("name") String name,
                                             @RequestParam("prenom") String prenom,
                                             @RequestParam("mail") String mail,
                                             @RequestParam("password") String password,
                                             @RequestParam("num_tel") String numero_telephone,
                                             @RequestParam("role") String role) {

        String response = userService.createUserByMobileApp(name,prenom,mail,password,numero_telephone,role);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @RequestMapping(value = "/activateByPhone/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> activateUserByPhone(@PathVariable("id") String id){

        String response = userService.activateUserByPhone(id);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @RequestMapping(value = "/getSessionIdByUsername/{username}/{client}", method = RequestMethod.GET, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<ResponseDTO> getSessionIdByUsername(@PathVariable("username") String username,
                                                              @PathVariable("client") Client client){

        String session_id = userService.getSessionIdByUsername(username,client);

        if(session_id == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseDTO(session_id),HttpStatus.OK);

    }

    @RequestMapping(value = "/setTokenForUser", method = RequestMethod.POST)
    public ResponseEntity<String> setTokenForUser(@RequestParam("session_id") String session_id,
                                                  @RequestParam("token_phone") String token_phone){

        String response = userService.setTokenPhoneForUser(session_id,token_phone);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<ResponseDTO> deleteUser(@RequestParam("username") String username){

        String response = userService.deleteUser(username);

        if (response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseDTO(response),HttpStatus.OK);

    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateUser(@RequestParam("name") String name,
                                           @RequestParam("prenom") String prenom,
                                           @RequestParam("session_id") String session_id,
                                           @RequestParam("password") String password,
                                           @RequestParam("numero_telephone") String num_telephone){

        String response = userService.updateUser(name,prenom,session_id,password,num_telephone);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseDTO(response),HttpStatus.OK);

    }

    @RequestMapping(value = "/checkIfPasswordCorrect", method = RequestMethod.POST, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<ResponseDTO> checkIfPasswordCorrect(@RequestParam("username") String username,
                                                          @RequestParam("password") String password){

        String isPasswordCorrect = userService.checkIfPasswordCorrect(username,password);

        if(isPasswordCorrect == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseDTO(isPasswordCorrect),HttpStatus.OK);

    }

    @RequestMapping(value = "/setFamilyForUser", method = RequestMethod.POST, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<ResponseDTO> setFamilyForUser(@RequestParam("username") String username,
                                                   @RequestParam("name_family") String name_family,
                                                   @RequestParam("password_family") String password_family){

        String response = familyService.setFamilyForUser(username,name_family,password_family);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseDTO(response),HttpStatus.OK);

    }

    @RequestMapping(value = "/getUserByName", method = RequestMethod.POST, produces = { "application/json" })
    public ResponseEntity<UserDto> getUserByName(@RequestParam(value = "username", required = false) String username){

        User user = userService.getUser(username);

        if(user == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new UserDto(user.getNom(),user.getPrenom(),user.getMail(),
                user.getCreation_date(), user.getNumero_telephone() != null ? user.getNumero_telephone() : "", user.getToken_telephone() != null ? user.getToken_telephone() : "", user.getProfile_picture_path() != null ? user.getProfile_picture_path() : "",
                user.getIsActivated() != null ? user.getIsActivated() : "Not Activated", user.getFamily() != null ? new FamilyDto(user.getFamily().getName(),user.getFamily().getCreation_date()) : new FamilyDto()),HttpStatus.OK);

    }

    @RequestMapping(value = "/getPathProfilePicture/{session_id}", method = RequestMethod.GET, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<ResponseDTO> getProfilePictureForUser(@PathVariable("session_id") String session_id) {

        String path = userService.getProfilePicture(session_id);

        if(path == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(new ResponseDTO(path),HttpStatus.OK);
    }

    @RequestMapping(value = "/setProfilePicture", method = RequestMethod.POST, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<ResponseDTO> setProfilePictureForUser(@RequestParam("session_id") String session_id,
                                                                @RequestParam("uploadfile") MultipartFile file) {

        String response = userService.setProfilePicture(session_id,file);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseDTO(response),HttpStatus.OK);

    }

    @RequestMapping(value = "/getUsersByFamilyName/{family_name}", method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<List<UserDto>> getUsersByFamilyId(@PathVariable("family_name") String family_name){

        List<User> users = familyService.getUsersByFamily(family_name);

        if (users == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(UserDto.getUsersDtoByList(users),HttpStatus.OK);

    }


}
