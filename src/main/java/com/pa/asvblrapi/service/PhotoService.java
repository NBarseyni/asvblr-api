package com.pa.asvblrapi.service;

import com.pa.asvblrapi.entity.Photo;
import com.pa.asvblrapi.entity.Team;
import com.pa.asvblrapi.exception.PhotoNotFoundException;
import com.pa.asvblrapi.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;

    @Value("${asvblrapi.app.photoFolder}")
    private String UPLOADED_FOLDER;

    public List<Photo> getAllPhotos() {
        return this.photoRepository.findAll();
    }

    public Optional<Photo> getPhoto(Long id) {
        return this.photoRepository.findById(id);
    }

    public Photo createPhoto(MultipartFile file) throws IOException {
        try {
            String fileName = file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + fileName);
            Files.write(path, bytes);

            Photo photo = new Photo(fileName);
            return this.photoRepository.save(photo);
        }
        catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public Photo createTeamPhoto(MultipartFile file, Team team) throws IOException {
        try {
            String fileName = file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + fileName);
            Files.write(path, bytes);

            Photo photo = new Photo(fileName, team);
            return this.photoRepository.save(photo);
        }
        catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public void deletePhoto(Long id) throws PhotoNotFoundException, IOException {
        Optional<Photo> photo = this.photoRepository.findById(id);
        if(!photo.isPresent()) {
            throw new PhotoNotFoundException(id);
        }
        this.photoRepository.delete(photo.get());
        try {
            Files.delete(Paths.get(UPLOADED_FOLDER + photo.get().getName()));
        }
        catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }
}
