package com.worldgether.mbal.controller;

import com.worldgether.mbal.model.Evt_message;
import com.worldgether.mbal.model.Message;
import com.worldgether.mbal.model.User;
import com.worldgether.mbal.model.dto.ResponseDTO;
import com.worldgether.mbal.service.EventService;
import com.worldgether.mbal.service.UserService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

//TODO : Livrer en prod

@Controller
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/add/{message_sent}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<ResponseDTO> send(@RequestParam("username") String username,
                                         @PathVariable("message_sent") Message message) throws JSONException {

        String response = eventService.addEvent(username,message);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseDTO(response),HttpStatus.OK);

    }

    @RequestMapping(value = "/changeWifiSettings", method = RequestMethod.POST, produces = { "application/json" })
    public ResponseEntity<ResponseDTO> changeWifiSettings(@RequestParam("name_mbal") String name_mbal){

        String changeWifi = eventService.changeWifiSettings(name_mbal);

        if(changeWifi == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseDTO(changeWifi),HttpStatus.OK);

    }

    @RequestMapping(value = "/getAllEventsByFamily/{family_name}", method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<List<Evt_message>> getAllEventsByFamily(@PathVariable("family_name") String family_name){

        List<Evt_message> events = eventService.getAllEventsByFamily(family_name);

        if(events == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(events,HttpStatus.OK);

    }

    @RequestMapping(value = "/getLastEventByFamily/{family_name}", method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<Evt_message> getLastEventByFamily(@PathVariable("family_name") String family_name){

        Evt_message event = eventService.getLastEventByFamily(family_name);

        if(event == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(event, HttpStatus.OK);

    }

    @RequestMapping(value = "/getEventAtDate/{family_name}", method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<Evt_message> getEventAtDateByFamily(@PathVariable("family_name") String family_name,
                                                            @RequestParam("date") Timestamp date){

        Evt_message event = eventService.getEventAtDateByFamily(family_name, date);

        if(event == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(event, HttpStatus.OK);

    }

}
