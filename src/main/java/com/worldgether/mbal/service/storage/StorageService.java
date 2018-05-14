package com.worldgether.mbal.service.storage;

import com.worldgether.mbal.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {

    private Path rootLocation;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public StorageService(@Value("${api.files.baseDir}") String path){
        this.init(path);
    }

    public void store(MultipartFile file) throws IOException {
        Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
    }

    public Resource loadFile(String filename) throws MalformedURLException, NotFoundException {
        Path file = rootLocation.resolve(filename);
        Resource resource = new UrlResource(file.toUri());
        if(resource.exists() || resource.isReadable()) {
            return resource;
        }else{
            throw new NotFoundException("Image doesn't exist!");
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void deleteSpecificFile(String filename) throws IOException {
        Files.delete(this.rootLocation.resolve(filename));
    }

    private void init(String path) {
        try {
            rootLocation = Paths.get(path);
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
