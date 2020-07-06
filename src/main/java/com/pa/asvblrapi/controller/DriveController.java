package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.DriveDto;
import com.pa.asvblrapi.dto.DriveUserDto;
import com.pa.asvblrapi.dto.UserDtoFirebase;
import com.pa.asvblrapi.exception.*;
import com.pa.asvblrapi.service.DriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/drives")
public class DriveController {
    @Autowired
    private DriveService driveService;

    @GetMapping("")
    public List<DriveDto> getAllDrives() {
        return this.driveService.getAllDrive();
    }

    @GetMapping("/{id}")
    public DriveDto getDrive(@PathVariable Long id) {
        return this.driveService.getDrive(id);
    }

    @PostMapping("")
    public ResponseEntity<Object> createDrive(@Valid @RequestBody DriveDto driveDto) {
        try {
            DriveDto drive = this.driveService.createDrive(driveDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(drive);
        } catch (UserNotFoundException | MatchNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserAlreadyInADriveException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDrive(@PathVariable Long id, @Valid @RequestBody DriveDto driveDto) {
        try {
            DriveDto drive = this.driveService.updateDrive(id, driveDto);
            return ResponseEntity.status(HttpStatus.OK).body(drive);
        } catch (DriveNotFoundException | UserNotFoundException | MatchNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/passengers")
    public ResponseEntity<Object> getPassengers(@PathVariable Long id) {
        try {
            List<UserDtoFirebase> passengers = this.driveService.getPassengers(id);
            return ResponseEntity.status(HttpStatus.OK).body(passengers);
        } catch (DriveNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/passengers")
    public ResponseEntity<Object> addPassenger(@PathVariable Long id, @Valid @RequestBody DriveUserDto driveUserDto) {
        try {
            DriveDto driveDto = this.driveService.addPassenger(id, driveUserDto.getIdUser());
            return ResponseEntity.status(HttpStatus.OK).body(driveDto);
        } catch (DriveNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DriveIsFullException | UserAlreadyInDriveException | UserAlreadyInADriveException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idDrive}/passengers/{idUser}")
    public ResponseEntity<Object> deletePassenger(@PathVariable Long idDrive, @PathVariable Long idUser) {
        try {
            DriveDto driveDto = this.driveService.deletePassenger(idDrive, idUser);
            return ResponseEntity.status(HttpStatus.OK).body(driveDto);
        } catch (DriveNotFoundException | UserNotFoundException | UserNotFoundInDriveException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDrive(@PathVariable Long id) {
        try {
            this.driveService.deleteDrive(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (DriveNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
