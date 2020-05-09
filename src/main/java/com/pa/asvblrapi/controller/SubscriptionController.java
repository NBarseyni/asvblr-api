package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.SubscriptionDto;
import com.pa.asvblrapi.entity.Document;
import com.pa.asvblrapi.entity.Player;
import com.pa.asvblrapi.entity.Subscription;
import com.pa.asvblrapi.entity.User;
import com.pa.asvblrapi.exception.SubscriptionNotFoundException;
import com.pa.asvblrapi.repository.UserRepository;
import com.pa.asvblrapi.service.DocumentService;
import com.pa.asvblrapi.service.PlayerService;
import com.pa.asvblrapi.service.SubscriptionService;
import com.pa.asvblrapi.spring.EmailServiceImpl;
import com.pa.asvblrapi.spring.RandomPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private EmailServiceImpl emailService;

    private RandomPasswordGenerator randomPasswordGenerator = new RandomPasswordGenerator();

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("")
    public List<Subscription> getSubscriptions() {
        return this.subscriptionService.getAllSubscriptions();
    }

    @GetMapping("/{id}")
    public Subscription getSubscription(@PathVariable Long id) {
        return this.subscriptionService.getSubscription(id)
                .orElseThrow(() -> new SubscriptionNotFoundException(id));
    }

    @PostMapping("")
    public ResponseEntity<Subscription> createSubscription(@Valid @RequestBody SubscriptionDto subscriptionDto) {
        try {
            Subscription subscription = this.subscriptionService.createSubscription(subscriptionDto);
            this.emailService.sendMessageCreateSubscription(subscription);
            return ResponseEntity.status(HttpStatus.CREATED).body(subscription);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSubscription(@PathVariable Long id, @Valid @RequestBody SubscriptionDto subscriptionDto) {
        try {
            Subscription subscription = this.subscriptionService.updateSubscription(id, subscriptionDto);
            return ResponseEntity.status(HttpStatus.OK).body(subscription);
        }
        catch (SubscriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(path = "/{id}/cni")
    public ResponseEntity<Object> addCNI(@RequestPart("file") MultipartFile file, @PathVariable Long id) {
        this.subscriptionService.getSubscription(id).orElseThrow(() -> new SubscriptionNotFoundException(id));
        try {
            Document document = this.documentService.createDocument(file);
            this.subscriptionService.addCNI(id, document);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/identity-photo")
    public ResponseEntity<Object> addIdentityPhoto(@RequestPart("file") MultipartFile file, @PathVariable Long id) {
        this.subscriptionService.getSubscription(id).orElseThrow(() -> new SubscriptionNotFoundException(id));
        try {
            Document document = this.documentService.createDocument(file);
            this.subscriptionService.addIdentityPhoto(id, document);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/medical-certificate")
    public ResponseEntity<Object> addMedicalCertificate(@RequestPart("file") MultipartFile file, @PathVariable Long id) {
        this.subscriptionService.getSubscription(id).orElseThrow(() -> new SubscriptionNotFoundException(id));
        try {
            Document document = this.documentService.createDocument(file);
            this.subscriptionService.addMedicalCertificate(id, document);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/confirmed")
    public ResponseEntity<Object> confirmedSubscription(@PathVariable Long id) {
        try {
            Subscription subscription = this.subscriptionService.confirmedSubscription(id);
            String username = String.format("%s%s", subscription.getFirstName(), subscription.getLastName());
            int i = 1;
            while(userRepository.existsByUsername(username)) {
                username += i;
            }
            String password = randomPasswordGenerator.generatePassword();
            User user = userRepository.save(new User(username, subscription.getFirstName(), subscription.getLastName(),
                    subscription.getEmail(), encoder.encode(password)));
            Player player = this.playerService.createPlayer(subscription, user);
            this.subscriptionService.setPlayer(id, player);
            this.emailService.sendMessageCreateUser(user, password);
        }
        catch (SubscriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PatchMapping("/{id}/unconfirmed")
    public ResponseEntity<Object> unconfirmedSubscription(@PathVariable Long id) {
        try {
            this.subscriptionService.unconfirmedSubscription(id);
        }
        catch (SubscriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSubscription(@PathVariable Long id) {
        try {
            this.subscriptionService.deleteSubscription(id);
        }
        catch (SubscriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
