package com.worldgether.mbal.controller;

import com.worldgether.mbal.model.Evt_message;
import com.worldgether.mbal.model.Message;
import com.worldgether.mbal.model.dto.ResponseDTO;
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

import java.util.List;

@Controller
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @RequestMapping(value = "/add/{message_sent}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<ResponseDTO> send(@RequestParam("username") String username,
                                         @PathVariable("message_sent") Message message) throws JSONException {

        String response = eventService.addEvent(username,message);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseDTO(response),HttpStatus.OK);

    }

    @RequestMapping(value = "/getAllEventsByFamily/{family_name}", method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<List<Evt_message>> getAllEventsByFamily(@PathVariable("family_name") String family_name){

        List<Evt_message> events = eventService.getAllEventsByFamily(family_name);

        if(events == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(events,HttpStatus.OK);

    }

}
