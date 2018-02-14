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
@RequestMapping("/family")
public class FamilyController {

    @Autowired
    private FamilyService familyService;

    @Autowired
    private FamilyRepository familyRepository;

    @PostMapping("/createFamily")
    public ResponseEntity<Family> createFamily(@RequestParam("name") String name,
                                              @RequestParam("password") String password,
                                              @RequestParam("id_user") Integer id_user){

        Family newFamily = familyService.createFamily(id_user,name,password);

        if (newFamily == null){
            return new ResponseEntity<Family>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Family>(newFamily,HttpStatus.OK);

    }

    @PostMapping("/updateNameFamily")
    public ResponseEntity<Family> updateNameFamily(@RequestParam("id_family") Integer id_family,
                                                   @RequestParam("name") String name){

        Family family = familyService.updateNameFamily(id_family,name);

        if(family == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Family>(family,HttpStatus.OK);

    }

    @PostMapping("/updatePasswordFamily")
    public ResponseEntity<Family> updatePasswordFamily(@RequestParam("id_family") Integer id_family,
                                                   @RequestParam("password") String newPassword){

        Family family = familyService.updatePasswordFamily(id_family,newPassword);

        if(family == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Family>(family,HttpStatus.OK);

    }

    @PostMapping("/deleteFamily")
    public ResponseEntity<String> deleteFamily(@RequestParam("id_family") Integer id_family){

        String response = familyService.deleteFamily(id_family);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(response,HttpStatus.OK);

    }

    @GetMapping("/getFamily/{id_family}")
    public ResponseEntity<Family> getFamily(@PathVariable("id_family") Integer id_family){

        Family family = familyService.getFamily(id_family);

        if(family == null){
            return new ResponseEntity<Family>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Family>(family,HttpStatus.OK);

    }

}
