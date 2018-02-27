package com.worldgether.mbal.controller;

import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.repository.FamilyRepository;
import com.worldgether.mbal.service.FamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/api/family")
public class FamilyController {

    @Autowired
    private FamilyService familyService;

    @Autowired
    private FamilyRepository familyRepository;

    @PostMapping("/createFamily")
    public ResponseEntity<String> createFamily(@RequestParam("familyname") String name,
                                              @RequestParam("password") String password,
                                              @RequestParam("session_id") String session_id){

        String response = familyService.createFamily(session_id,name,password);

        if (response == null){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(response,HttpStatus.OK);

    }

    @PostMapping("/updatePasswordFamily")
    public ResponseEntity<String> updatePasswordFamily(@RequestParam("session_id") String session_id,
                                                   @RequestParam("new_password") String newPassword){

        String response = familyService.updatePasswordFamily(session_id,newPassword);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @PostMapping("/checkIfPasswordFamilyCorrect")
    public ResponseEntity<String> checkIfPasswordFamilyCorrect(@RequestParam("session_id") String session_id,
                                                               @RequestParam("password") String password){

        String response = familyService.checkIfPasswordFamilyCorrect(session_id,password);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @PostMapping("/deleteFamily")
    public ResponseEntity<String> deleteFamily(@RequestParam("name") String name){

        String response = familyService.deleteFamily(name);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(response,HttpStatus.OK);

    }

    @GetMapping("/getFamilyByName/{name}")
    public ResponseEntity<Family> getFamily(@PathVariable("name") String name){

        Family family = familyService.getFamily(name);

        if(family == null){
            return new ResponseEntity<Family>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Family>(family,HttpStatus.OK);

    }

}
