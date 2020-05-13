package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.SubscriptionDto;
import com.pa.asvblrapi.entity.Document;
import com.pa.asvblrapi.entity.Player;
import com.pa.asvblrapi.entity.Subscription;
import com.pa.asvblrapi.entity.User;
import com.pa.asvblrapi.exception.SeasonNotFoundException;
import com.pa.asvblrapi.exception.SubscriptionAlreadyValidatedException;
import com.pa.asvblrapi.exception.SubscriptionNotFoundException;
import com.pa.asvblrapi.mapper.SubscriptionMapper;
import com.pa.asvblrapi.service.*;
import com.pa.asvblrapi.spring.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private UserService userService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private EmailServiceImpl emailService;

    @GetMapping("")
    public List<SubscriptionDto> getSubscriptions() {
        return SubscriptionMapper.instance.toDto(this.subscriptionService.getAllSubscriptions());
    }

    @GetMapping("/{id}")
    public SubscriptionDto getSubscription(@PathVariable Long id) {
        return SubscriptionMapper.instance.toDto(this.subscriptionService.getSubscription(id)
                .orElseThrow(() -> new SubscriptionNotFoundException(id)));
    }

    @PostMapping("")
    public ResponseEntity<Object> createSubscription(@Valid @RequestBody SubscriptionDto subscriptionDto) {
        try {
            Subscription subscription = this.subscriptionService.createSubscription(subscriptionDto);
            this.emailService.sendMessageCreateSubscription(subscription);
            return ResponseEntity.status(HttpStatus.CREATED).body(SubscriptionMapper.instance.toDto(subscription));
        }
        catch (SeasonNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No current season");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSubscription(@PathVariable Long id, @Valid @RequestBody SubscriptionDto subscriptionDto) {
        try {
            Subscription subscription = this.subscriptionService.updateSubscription(id, subscriptionDto);
            return ResponseEntity.status(HttpStatus.OK).body(SubscriptionMapper.instance.toDto(subscription));
        }
        catch (SubscriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{id}/cni")
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

    @PatchMapping("/{id}/validated")
    public ResponseEntity<Object> validatedSubscription(@PathVariable Long id) {
        try {
            Subscription subscription = this.subscriptionService.validatedSubscription(id);
            User user = this.userService.createUser(subscription.getFirstName(), subscription.getLastName(), subscription.getEmail());
            Player player = this.playerService.createPlayer(subscription, user);
            this.subscriptionService.setPlayer(id, player);
        }
        catch (SubscriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (SubscriptionAlreadyValidatedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PatchMapping("/{id}/unvalidated")
    public ResponseEntity<Object> unvalidatedSubscription(@PathVariable Long id) {
        try {
            this.subscriptionService.unvalidatedSubscription(id);
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
