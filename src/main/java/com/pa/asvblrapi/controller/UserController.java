package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.*;
import com.pa.asvblrapi.entity.Player;
import com.pa.asvblrapi.entity.User;
import com.pa.asvblrapi.exception.*;
import com.pa.asvblrapi.repository.UserRepository;
import com.pa.asvblrapi.service.*;
import com.pa.asvblrapi.spring.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DriveService driveService;

    @Autowired
    private PlayerService playerService;

    @GetMapping("")
    public List<UserDto> getAllUser() {
        return this.userService.getAllUser();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return this.userService.getUser(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("/{id}/driving-drives")
    public ResponseEntity<Object> getDrivingDrive(@PathVariable Long id) {
        try {
            List<DriveDto> drives = this.driveService.getAllByIdDriver(id);
            return ResponseEntity.status(HttpStatus.OK).body(drives);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/passenger-drives")
    public ResponseEntity<Object> getDrives(@PathVariable Long id) {
        try {
            List<DriveDto> drives = this.driveService.getAllByIdPassenger(id);
            return ResponseEntity.status(HttpStatus.OK).body(drives);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/player")
    public ResponseEntity<Object> getPlayer(@PathVariable Long id) {
        try {
            PlayerDto playerDto = this.playerService.getPlayerByIdUser(id);
            return ResponseEntity.status(HttpStatus.OK).body(playerDto);
        } catch (PlayerNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/teams")
    public ResponseEntity<Object> getTeams(@PathVariable Long id) {
        try {
            List<TeamListDto> teams = this.userService.getUserTeams(id);
            return ResponseEntity.status(HttpStatus.OK).body(teams);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserIsNotPlayerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        try {
            User user = this.userService.updateUser(id, userDto);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/give-manager-right")
    public ResponseEntity<Object> giveManagerRight(@PathVariable Long id) {
        try {
            UserDto user = this.userService.giveManagerRight(id);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserAlreadyManagerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/remove-manager-right")
    public ResponseEntity<Object> removeManagerRight(@PathVariable Long id) {
        try {
            UserDto user = this.userService.removeManagerRight(id);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/give-president-right")
    public ResponseEntity<Object> givePresidentRight(@PathVariable Long id) {
        try {
            UserDto user = this.userService.givePresidentRight(id);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        try {
            this.userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserIsPresidentException | UserIsPlayerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ===== NON-API =====

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
