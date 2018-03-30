package com.worldgether.mbal.controller;

import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.model.dto.ResponseDTO;
import com.worldgether.mbal.repository.FamilyRepository;
import com.worldgether.mbal.service.FamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/api/family")
public class FamilyController {

    @Autowired
    private FamilyService familyService;

    @Autowired
    private FamilyRepository familyRepository;

    @RequestMapping(value = "/createFamily", method = RequestMethod.POST, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<ResponseDTO> createFamily(@RequestParam("familyname") String name,
                                                    @RequestParam("password") String password,
                                                    @RequestParam("session_id") String session_id){

        String response = familyService.createFamily(session_id,name,password);

        if (response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseDTO(response),HttpStatus.OK);

    }

    @RequestMapping(value = "/updatePasswordFamily", method = RequestMethod.POST, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<ResponseDTO> updatePasswordFamily(@RequestParam("session_id") String session_id,
                                                   @RequestParam("new_password") String newPassword){

        String response = familyService.updatePasswordFamily(session_id,newPassword);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseDTO(response),HttpStatus.OK);

    }

    @RequestMapping(value = "/checkIfPasswordFamilyCorrect", method = RequestMethod.POST, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<ResponseDTO> checkIfPasswordFamilyCorrect(@RequestParam("session_id") String session_id,
                                                               @RequestParam("password") String password){

        String response = familyService.checkIfPasswordFamilyCorrect(session_id,password);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseDTO(response),HttpStatus.OK);

    }

    @RequestMapping(value = "/deleteFamily", method = RequestMethod.POST, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<ResponseDTO> deleteFamily(@RequestParam("name") String name){

        String response = familyService.deleteFamily(name);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseDTO(response),HttpStatus.OK);

    }

    @RequestMapping(value = "/getFamilyByName/{name}", method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<Family> getFamily(@PathVariable("name") String name){

        Family family = familyService.getFamily(name);

        if(family == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(family,HttpStatus.OK);

    }

    @RequestMapping(value = "/getPathFamilyProfilePicture/{family_name}", method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<List<String>> getProfilePictureForFamily(@PathVariable("family_name") String name) {

        List<String> path = familyService.getPathProfilePictureForFamily(name);

        if(path == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(path,HttpStatus.OK);
    }

}
