package com.worldgether.mbal.controller;

import com.worldgether.mbal.model.Evt_message;
import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.service.EventService;
import com.worldgether.mbal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/amazon")
@PreAuthorize("hasRole('ROLE_USER')")
public class AmazonController {

    @Autowired
    private UserService userService;

    @Autowired
    EventService eventService;

    @GetMapping(value = "/getEventAtDateAmazon")
    public ResponseEntity<Evt_message> getEventAtDateAmazon(Principal principal){

        Evt_message event = null;
        Family familyUser = userService.getUser(principal.getName()).getFamily();

        if(familyUser != null){
           event = eventService.getLastEventByFamily(familyUser.getName());
        }

        if(event == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(event, HttpStatus.OK);

    }

}
