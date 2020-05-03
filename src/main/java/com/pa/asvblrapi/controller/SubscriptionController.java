package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.SubscriptionDto;
import com.pa.asvblrapi.entity.Subscription;
import com.pa.asvblrapi.entity.User;
import com.pa.asvblrapi.exception.SubscriptionNotFoundException;
import com.pa.asvblrapi.repository.UserRepository;
import com.pa.asvblrapi.service.SubscriptionService;
import com.pa.asvblrapi.spring.EmailServiceImpl;
import com.pa.asvblrapi.spring.RandomPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    private final RandomPasswordGenerator randomPasswordGenerator = new RandomPasswordGenerator();

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public List<Subscription> getSubscriptions() {
        return this.subscriptionService.getAllSubscriptions();
    }

    @GetMapping("/{id}")
    public Subscription getSubscription(@PathVariable Long id) {
        return this.subscriptionService.getSubscription(id)
                .orElseThrow(() -> new SubscriptionNotFoundException(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Subscription> create(@Valid @RequestBody SubscriptionDto subscriptionDto) {
        try {
            Subscription subscription = this.subscriptionService.createSubscription(subscriptionDto);
            String username = subscription.getFirstName() + subscription.getLastName();

            while(this.userRepository.existsByUsername(username)) {
                username += "1";
            }

            User user = this.userRepository.save(
                    new User(username, subscription.getFirstName(), subscription.getLastName(), subscription.getEmail(),
                            randomPasswordGenerator.generatePassword()));

            String text = String.format("Bonjour, voici vos identifiants : \n Username : %s \n Password : %s",
                    username, user.getPassword());

            this.emailService.sendSimpleMessage(subscription.getEmail(), "Inscription" , text);
            return ResponseEntity.status(HttpStatus.CREATED).body(subscription);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody SubscriptionDto subscriptionDto) {
        try {
            Subscription subscription = this.subscriptionService.updateSubscription(id, subscriptionDto);
            return ResponseEntity.status(HttpStatus.OK).body(subscription);
        }
        catch (SubscriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
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
