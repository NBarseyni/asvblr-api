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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    private final RandomPasswordGenerator randomPasswordGenerator = new RandomPasswordGenerator();

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

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

            String password = randomPasswordGenerator.generatePassword();

            this.userRepository.save(new User(username, subscription.getFirstName(), subscription.getLastName(),
                    subscription.getEmail(), encoder.encode(password)));

            Map<String, Object> map = new HashMap<>();
            map.put("name", String.format("%s %s", subscription.getFirstName(), subscription.getLastName()));
            map.put("username", username);
            map.put("password", password);

            this.emailService.sendMessageUsingThymeleafTemplate(subscription.getEmail(),
                    "Inscription Association sportive de Volley Ball de Bourg-la-Reine", map);
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
