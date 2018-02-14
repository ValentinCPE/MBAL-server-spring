package com.worldgether.mbal.controller;

import com.worldgether.mbal.model.Family;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FamilyService familyService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestParam("name") String name,
                                           @RequestParam("prenom") String prenom,
                                           @RequestParam("mail") String mail,
                                           @RequestParam("password") String password,
                                           @RequestParam("num_tel") String numero_telephone) {

        User newUser = userService.createUser(name,prenom,mail,password,numero_telephone);

        if(newUser == null){
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<User>(newUser,HttpStatus.OK);

    }

    @PostMapping("/setTokenForUser")
    public ResponseEntity<User> setTokenForUser(@RequestParam("id_user") Integer id_user,
                                                @RequestParam("token_phone") String token_phone){

        User userEdited = userService.setTokenPhoneForUser(id_user,token_phone);

        if(userEdited == null){
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<User>(userEdited,HttpStatus.OK);

    }

    @PostMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam("id_user") Integer id_user){

        String response = userService.deleteUser(id_user);

        if (response == null){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(response,HttpStatus.OK);

    }

    @PostMapping("/updateUser")
    public ResponseEntity<User> updateUser(@RequestParam("id_user") Integer id_user,
                                           @RequestParam("name") String name,
                                           @RequestParam("prenom") String prenom,
                                           @RequestParam("mail") String mail,
                                           @RequestParam("password") String password,
                                           @RequestParam("numero_telephone") String num_telephone){

        User userUpdated = userService.updateUser(id_user,name,prenom,mail,password,num_telephone);

        if(userUpdated == null){
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<User>(userUpdated,HttpStatus.OK);

    }

    @PostMapping("/checkIfPasswordCorrect")
    public ResponseEntity<Boolean> checkIfPasswordCorrect(@RequestParam("id_user") Integer id_user,
                                                          @RequestParam("password") String password){

        Boolean isPasswordCorrect = userService.checkIfPasswordCorrect(id_user,password);

        if(isPasswordCorrect == null){
            return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Boolean>(isPasswordCorrect,HttpStatus.OK);

    }

    @PostMapping("/setFamilyForUser")
    public ResponseEntity<String> setFamilyForUser(@RequestParam("id_user") Integer id_user,
                                                   @RequestParam("id_family") Integer id_family,
                                                   @RequestParam("password_family") String password_family){

        String response = familyService.setFamilyForUser(id_user,id_family,password_family);

        if(response == null){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(response,HttpStatus.OK);

    }

    @GetMapping("/getUserById/{id_user}")
    public ResponseEntity<User> getUserById(@PathVariable("id_user") Integer id_user){

        User user = userService.getUser(id_user);

        if(user == null){
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<User>(user,HttpStatus.OK);

    }

    @GetMapping("/getUsersByFamilyId/{family_id}")
    public ResponseEntity<List<User>> getUsersByFamilyId(@PathVariable("family_id") Integer family_id){

        List<User> users = familyService.getUsersByFamily(family_id);

        if (users == null){
            return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<List<User>>(users,HttpStatus.OK);

    }


}
