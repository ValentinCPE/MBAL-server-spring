package com.worldgether.mbal.controller;

import com.worldgether.mbal.model.Message;
import com.worldgether.mbal.service.EventService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @RequestMapping(value = "/add/{message_sent}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> send(@RequestParam("username") String username,
                                       @PathVariable("message_sent") Message message) throws JSONException {

        String response = eventService.addEvent(username,message);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response,HttpStatus.OK);

    }
}
