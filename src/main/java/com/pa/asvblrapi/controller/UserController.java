package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.UserDto;
import com.pa.asvblrapi.dto.UserDtoFirebase;
import com.pa.asvblrapi.entity.User;
import com.pa.asvblrapi.exception.InvalidOldPasswordException;
import com.pa.asvblrapi.exception.UserNotFoundException;
import com.pa.asvblrapi.repository.UserRepository;
import com.pa.asvblrapi.service.FirebaseService;
import com.pa.asvblrapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return this.userService.getUser(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PostMapping("/update-password")
    public ResponseEntity<Object> changeUserPassword(
            @RequestParam("password") String password,
            @RequestParam("oldPassword") String oldPassword) {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!this.userService.checkIfValidOldPassword(user, oldPassword)) {
            throw new InvalidOldPasswordException();
        }
        this.userService.changeUserPassword(user, password);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        try {
            User user = this.userService.updateUser(id, userDto);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        this.userService.deleteUser(id);
    }
}
