package com.worldgether.mbal.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.worldgether.mbal.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Controller
public class FileUploadController {

    @Autowired
    private StorageService storageService;

    List<String> files = new ArrayList<String>();

    // Multiple file upload
    @PostMapping("/uploadfile")
    public ResponseEntity<String> uploadFileMulti(@RequestParam("uploadfile") MultipartFile file) {
        try {
            storageService.store(file);
            files.add(file.getOriginalFilename());
            return new ResponseEntity<>("You successfully uploaded - " + file.getOriginalFilename(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("FAIL! Maybe You had uploaded the file before or the file's size > 500KB",HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getallfiles")
    public ResponseEntity<List<String>> getListFiles() {
        List<String> lstFiles = new ArrayList<String>();

        try{
            lstFiles = files.stream()
                    .map(fileName -> MvcUriComponentsBuilder
                            .fromMethodName(FileUploadController.class, "getFile", fileName).build().toString())
                    .collect(Collectors.toList());
        }catch(Exception e){
            throw e;
        }

        return new ResponseEntity<>(lstFiles,HttpStatus.OK);
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

}
