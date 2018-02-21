package com.worldgether.mbal.controller;

import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.model.Role;
import com.worldgether.mbal.model.User;
import com.worldgether.mbal.repository.UserRepository;
import com.worldgether.mbal.service.FamilyService;
import com.worldgether.mbal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FamilyService familyService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestParam("name") String name,
                                           @RequestParam("prenom") String prenom,
                                           @RequestParam("mail") String mail,
                                           @RequestParam("password") String password,
                                           @RequestParam("num_tel") String numero_telephone,
                                           @RequestParam("role") String role) {

        String response = userService.createUser(name,prenom,mail,password,numero_telephone,role);

        if(response == null){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(response,HttpStatus.OK);

    }

    @PostMapping("/setTokenForUser")
    public ResponseEntity<String> setTokenForUser(@RequestParam("username") String username,
                                                @RequestParam("token_phone") String token_phone){

        String response = userService.setTokenPhoneForUser(username,token_phone);

        if(response == null){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(response,HttpStatus.OK);

    }

    @PostMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam("username") String username){

        String response = userService.deleteUser(username);

        if (response == null){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(response,HttpStatus.OK);

    }

    @PostMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestParam("name") String name,
                                           @RequestParam("prenom") String prenom,
                                           @RequestParam("mail") String mail,
                                           @RequestParam("password") String password,
                                           @RequestParam("numero_telephone") String num_telephone){

        String response = userService.updateUser(name,prenom,mail,password,num_telephone);

        if(response == null){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(response,HttpStatus.OK);

    }

    @PostMapping("/checkIfPasswordCorrect")
    public ResponseEntity<String> checkIfPasswordCorrect(@RequestParam("username") String username,
                                                          @RequestParam("password") String password){

        String isPasswordCorrect = userService.checkIfPasswordCorrect(username,password);

        if(isPasswordCorrect == null){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(isPasswordCorrect,HttpStatus.OK);

    }

    @PostMapping("/setFamilyForUser")
    public ResponseEntity<String> setFamilyForUser(@RequestParam("username") String username,
                                                   @RequestParam("name_family") String name_family,
                                                   @RequestParam("password_family") String password_family){

        String response = familyService.setFamilyForUser(username,name_family,password_family);

        if(response == null){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(response,HttpStatus.OK);

    }

    @GetMapping("/getUserByName/{username}")
    public ResponseEntity<User> getUserByName(@PathVariable("username") String username){

        User user = userService.getUser(username);

        if(user == null){
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<User>(user,HttpStatus.OK);

    }

    @GetMapping("/getUsersByFamilyName/{family_name}")
    public ResponseEntity<List<User>> getUsersByFamilyId(@PathVariable("family_name") String family_name){

        List<User> users = familyService.getUsersByFamily(family_name);

        if (users == null){
            return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<List<User>>(users,HttpStatus.OK);

    }


}
