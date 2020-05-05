package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.entity.Photo;
import com.pa.asvblrapi.exception.PhotoNotFoundException;
import com.pa.asvblrapi.service.PhotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/")
    public List<Photo> getPhotos() {
        return this.photoService.getAllPhotos();
    }

    @PostMapping("/create")
    public ResponseEntity<Photo> createPhoto(@RequestParam("file") MultipartFile multipartFile) {
        try {
            Photo photo = this.photoService.createPhoto(multipartFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(photo);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePhoto(@PathVariable Long id) {
        try {
            this.photoService.deletePhoto(id);
        }
        catch (PhotoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
