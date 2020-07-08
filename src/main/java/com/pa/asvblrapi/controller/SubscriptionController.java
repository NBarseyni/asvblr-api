package com.pa.asvblrapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pa.asvblrapi.dto.SubscriptionDto;
import com.pa.asvblrapi.dto.SubscriptionPaidDto;
import com.pa.asvblrapi.entity.*;
import com.pa.asvblrapi.exception.*;
import com.pa.asvblrapi.mapper.SubscriptionMapper;
import com.pa.asvblrapi.service.*;
import com.pa.asvblrapi.spring.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

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
        return this.subscriptionService.getCurrentSeasonSubscriptions();
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
        } catch (SeasonNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No current season");
        } catch (SubscriptionCategoryNotFoundException | PaymentModeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //@PostMapping("")
    public ResponseEntity<Object> createSubscriptionWithDocuments(@Valid @RequestParam String subscription,
                                                                  @RequestParam("cni") MultipartFile cniFile,
                                                                  @RequestParam("identityPhoto") MultipartFile identityPhotoFile,
                                                                  @RequestParam("medicalCertificate") MultipartFile medicalCertificateFile) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SubscriptionDto subscriptionDto = mapper.readValue(subscription, SubscriptionDto.class);
            Subscription subscriptionSave = this.subscriptionService.createSubscription(subscriptionDto);
            String username = this.userService.createUsername(subscriptionSave.getFirstName(), subscriptionSave.getLastName());
            Document cniDocument = this.documentService.createDocument(cniFile, username + "_cni");
            this.subscriptionService.addCNI(subscriptionSave.getId(), cniDocument);
            Document identityPhotoDocument = this.documentService.createDocument(identityPhotoFile,
                    username + "_identityPhoto");
            this.subscriptionService.addIdentityPhoto(subscriptionSave.getId(), identityPhotoDocument);
            Document medicalCertificateDocument = this.documentService.createDocument(medicalCertificateFile,
                    username + "_medicalCertificate");
            this.subscriptionService.addMedicalCertificate(subscriptionSave.getId(), medicalCertificateDocument);
            this.emailService.sendMessageCreateSubscription(subscriptionSave);
            return ResponseEntity.status(HttpStatus.CREATED).body(SubscriptionMapper.instance.toDto(subscriptionSave));
        } catch (IOException | MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSubscription(@PathVariable Long id, @Valid @RequestBody SubscriptionDto subscriptionDto) {
        try {
            Subscription subscription = this.subscriptionService.updateSubscription(id, subscriptionDto);
            return ResponseEntity.status(HttpStatus.OK).body(SubscriptionMapper.instance.toDto(subscription));
        } catch (SubscriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<Object> addDocuments(
            @PathVariable Long id,
            @RequestParam("cni") MultipartFile cniFile,
            @RequestParam("identityPhoto") MultipartFile identityPhotoFile,
            @RequestParam("medicalCertificate") MultipartFile medicalCertificateFile) {
        this.subscriptionService.getSubscription(id).orElseThrow(() -> new SubscriptionNotFoundException(id));
        try {
            Subscription subscription = this.subscriptionService.getSubscription(id)
                    .orElseThrow(() -> new SubscriptionNotFoundException(id));
            String username = this.userService.createUsername(subscription.getFirstName(), subscription.getLastName());
            Document cniDocument = this.documentService.createDocument(cniFile, username + "_cni");
            this.subscriptionService.addCNI(id, cniDocument);
            Document identityPhotoDocument = this.documentService.createDocument(identityPhotoFile,
                    username + "_identityPhoto");
            this.subscriptionService.addIdentityPhoto(id, identityPhotoDocument);
            Document medicalCertificateDocument = this.documentService.createDocument(medicalCertificateFile,
                    username + "_medicalCertificate");
            this.subscriptionService.addMedicalCertificate(id, medicalCertificateDocument);
            return ResponseEntity.status(HttpStatus.OK).body(SubscriptionMapper.instance.toDto(subscription));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/cni")
    public ResponseEntity<Object> addCNI(@RequestPart("file") MultipartFile file, @PathVariable Long id) {
        Subscription subscription = this.subscriptionService.getSubscription(id)
                .orElseThrow(() -> new SubscriptionNotFoundException(id));
        try {
            String username = this.userService.createUsername(subscription.getFirstName(), subscription.getLastName());
            Document document = this.documentService.createDocument(file, username + "_cni");
            this.subscriptionService.addCNI(id, document);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/identity-photo")
    public ResponseEntity<Object> addIdentityPhoto(@RequestPart("file") MultipartFile file, @PathVariable Long id) {
        Subscription subscription = this.subscriptionService.getSubscription(id)
                .orElseThrow(() -> new SubscriptionNotFoundException(id));
        try {
            String username = this.userService.createUsername(subscription.getFirstName(), subscription.getLastName());
            Document document = this.documentService.createDocument(file, username + "_identityPhoto");
            this.subscriptionService.addIdentityPhoto(id, document);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/medical-certificate")
    public ResponseEntity<Object> addMedicalCertificate(@RequestPart("file") MultipartFile file, @PathVariable Long id) {
        Subscription subscription = this.subscriptionService.getSubscription(id)
                .orElseThrow(() -> new SubscriptionNotFoundException(id));
        try {
            String username = this.userService.createUsername(subscription.getFirstName(), subscription.getLastName());
            Document document = this.documentService.createDocument(file, username + "_medicalCertificate");
            this.subscriptionService.addMedicalCertificate(id, document);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/payments")
    public List<SubscriptionPaidDto> getSubscriptionPayment(@PathVariable Long id) {
        return this.subscriptionService.getSubscriptionPaid(id);
    }

    @PatchMapping("/{idSubscription}/payments/{idPaymentMode}/validated")
    public ResponseEntity<Object> validatedPayment(@PathVariable Long idSubscription, @PathVariable Long idPaymentMode) {
        try {
            List<SubscriptionPaidDto> subscriptionsPaidDto =
                    this.subscriptionService.validatedPaymentMode(idSubscription, idPaymentMode);
            return ResponseEntity.status(HttpStatus.OK).body(subscriptionsPaidDto);
        } catch (SubscriptionPaidNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{idSubscription}/payments/{idPaymentMode}/unvalidated")
    public ResponseEntity<Object> unvalidatedPayment(@PathVariable Long idSubscription, @PathVariable Long idPaymentMode) {
        try {
            List<SubscriptionPaidDto> subscriptionsPaidDto =
                    this.subscriptionService.unvalidatedPaymentMode(idSubscription, idPaymentMode);
            return ResponseEntity.status(HttpStatus.OK).body(subscriptionsPaidDto);
        } catch (SubscriptionPaidNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/validated")
    public ResponseEntity<Object> validatedSubscription(@PathVariable Long id) {
        try {
            Subscription subscription = this.subscriptionService.validatedSubscription(id);
            User user = this.userService.createUserSubscription(subscription.getFirstName(), subscription.getLastName(),
                    subscription.getEmail());
            Player player = this.playerService.createPlayer(subscription, user);
            this.subscriptionService.setPlayer(id, player);
        } catch (SubscriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (SubscriptionAlreadyValidatedException | SubscriptionHasNotAllPaymentModeValidated e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSubscription(@PathVariable Long id) {
        try {
            this.subscriptionService.deleteSubscription(id);
        } catch (SubscriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
