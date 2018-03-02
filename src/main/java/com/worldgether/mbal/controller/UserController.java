package com.worldgether.mbal.controller;

import com.worldgether.mbal.model.Response;
import com.worldgether.mbal.model.Sessions;
import com.worldgether.mbal.model.User;
import com.worldgether.mbal.model.dto.FamilyDto;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> connect(@RequestParam("username") String username,
                                          @RequestParam("password") String password){

        String sessionId = userService.connect(username,password);

        if(sessionId == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(sessionId,HttpStatus.OK);

    }

    @RequestMapping(value = "/logout/{session_id}", method = RequestMethod.GET)
    public ResponseEntity<String> logout(@PathVariable("session_id") String session_id){

        String response = userService.logout(session_id);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestParam("name") String name,
                                           @RequestParam("prenom") String prenom,
                                           @RequestParam("mail") String mail,
                                           @RequestParam("password") String password,
                                           @RequestParam("num_tel") String numero_telephone,
                                           @RequestParam("role") String role,
                                           @RequestParam("file") MultipartFile file,
                                           RedirectAttributes redirectAttributes) {

        String response = userService.createUser(name,prenom,mail,password,numero_telephone,role);

        if(response == null){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(response,HttpStatus.OK);

    }

    @RequestMapping(value = "/setTokenForUser", method = RequestMethod.POST)
    public ResponseEntity<String> setTokenForUser(@RequestParam("session_id") String session_id,
                                                @RequestParam("token_phone") String token_phone){

        String response = userService.setTokenPhoneForUser(session_id,token_phone);

        if(response == null){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(response,HttpStatus.OK);

    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public ResponseEntity<String> deleteUser(@RequestParam("username") String username){

        String response = userService.deleteUser(username);

        if (response == null){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(response,HttpStatus.OK);

    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public ResponseEntity<String> updateUser(@RequestParam("name") String name,
                                           @RequestParam("prenom") String prenom,
                                           @RequestParam("session_id") String session_id,
                                           @RequestParam("password") String password,
                                           @RequestParam("numero_telephone") String num_telephone){

        String response = userService.updateUser(name,prenom,session_id,password,num_telephone);

        if(response == null){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(response,HttpStatus.OK);

    }

    @RequestMapping(value = "/checkIfPasswordCorrect", method = RequestMethod.POST)
    public ResponseEntity<String> checkIfPasswordCorrect(@RequestParam("username") String username,
                                                          @RequestParam("password") String password){

        String isPasswordCorrect = userService.checkIfPasswordCorrect(username,password);

        if(isPasswordCorrect == null){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(isPasswordCorrect,HttpStatus.OK);

    }

    @RequestMapping(value = "/setFamilyForUser", method = RequestMethod.POST)
    public ResponseEntity<String> setFamilyForUser(@RequestParam("session_id") String session_id,
                                                   @RequestParam("name_family") String name_family,
                                                   @RequestParam("password_family") String password_family){

        String response = familyService.setFamilyForUser(session_id,name_family,password_family);

        if(response == null){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(response,HttpStatus.OK);

    }

    @RequestMapping(value = "/getUserByName/{username}/", method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<UserDto> getUserByName(@PathVariable("username") String username){

        User user = userService.getUser(username);

        if(user == null){
            return new ResponseEntity<UserDto>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<UserDto>(new UserDto(user.getNom(),user.getPrenom(),user.getMail(),
                user.getCreation_date(), user.getNumero_telephone(), user.getToken_telephone(),
                new FamilyDto(user.getFamily().getName(),user.getFamily().getCreation_date())),HttpStatus.OK);

    }

    @RequestMapping(value = "/getUsersByFamilyName/{family_name}", method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<List<UserDto>> getUsersByFamilyId(@PathVariable("family_name") String family_name){

        List<User> users = familyService.getUsersByFamily(family_name);

        if (users == null){
            return new ResponseEntity<List<UserDto>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<List<UserDto>>(UserDto.getUsersDtoByList(users),HttpStatus.OK);

    }


}
