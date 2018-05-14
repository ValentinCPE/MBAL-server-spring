package com.worldgether.mbal.controller;

import com.worldgether.mbal.exception.NotFoundException;
import com.worldgether.mbal.model.dto.ResponseDTO;
import com.worldgether.mbal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/public")
public class PublicController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {

        Resource file = null;
        try {
            file = userService.getFile(filename);

            if(file == null){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/activate/{id}", method = RequestMethod.GET, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity<ResponseDTO> activateUser(@PathVariable("id") String id){

        String response = userService.activateUser(id);

        if(response == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ResponseDTO(response),HttpStatus.OK);

    }

}
