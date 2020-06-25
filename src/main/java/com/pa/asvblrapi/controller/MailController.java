package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.SendMailDto;
import com.pa.asvblrapi.entity.User;
import com.pa.asvblrapi.exception.UserNotFoundException;
import com.pa.asvblrapi.service.UserService;
import com.pa.asvblrapi.spring.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/mails")
public class MailController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailServiceImpl emailService;

    @PostMapping("/send-mail")
    public ResponseEntity<Object> sendMail(@Valid @RequestBody SendMailDto dto) {
        try {
            List<User> users = this.userService.convertListIdUserToUser(dto.getIdsUser());
            this.emailService.sendMessage(users, dto.getObject(), dto.getContent());
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
