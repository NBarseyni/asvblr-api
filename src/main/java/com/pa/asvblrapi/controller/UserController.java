package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.*;
import com.pa.asvblrapi.entity.Player;
import com.pa.asvblrapi.entity.User;
import com.pa.asvblrapi.exception.InvalidOldPasswordException;
import com.pa.asvblrapi.exception.PlayerNotFoundException;
import com.pa.asvblrapi.exception.UserAlreadyManagerException;
import com.pa.asvblrapi.exception.UserNotFoundException;
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
    private EmailServiceImpl emailService;

    @Autowired
    private UserSecurityService userSecurityService;

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
        } catch (PlayerNotFoundException |UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<Object> changeUserPassword(@Valid @RequestBody UserUpdatePasswordDto dto) {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!this.userService.checkIfValidOldPassword(user, dto.getOldPassword())) {
            throw new InvalidOldPasswordException();
        }
        UserDto userDto = this.userService.changeUserPassword(user, dto.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(HttpServletRequest request, @Valid @RequestBody UserResetPasswordDto dto) {
        try {
            User user = this.userService.getUserByEmail(dto.getEmail());
            if (user == null) {
                throw new UserNotFoundException(dto.getEmail());
            }
            String token = UUID.randomUUID().toString();
            this.userService.createPasswordResetTokenForUser(user, token);
            this.emailService.sendMessageResetPassword(token, user);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestParam("token") String token) {
        String result = this.userSecurityService.validatePasswordResetToken(token);
        if (result != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This token is invalid");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    @PostMapping("/save-password")
    public ResponseEntity<Object> savePassword(
            @RequestParam("token") String token,
            @RequestParam("password") String password) {
        String result = this.userSecurityService.validatePasswordResetToken(token);

        if (result != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This token is invalid");
        }

        Optional<User> user = this.userSecurityService.getUserByPasswordResetToken(token);
        if (user.isPresent()) {
            this.userService.changeUserPassword(user.get(), password);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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
    public ResponseEntity<Object> setManager(@PathVariable Long id) {
        try {
            UserDto user = this.userService.giveManagerRight(id);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserAlreadyManagerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
    public void deleteUser(@PathVariable Long id) {
        this.userService.deleteUser(id);
    }

    // ===== NON-API =====

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
