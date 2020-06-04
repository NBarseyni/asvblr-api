package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.entity.SubscriptionCategory;
import com.pa.asvblrapi.exception.SubscriptionCategoryNotFoundException;
import com.pa.asvblrapi.repository.SubscriptionCategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/subscription-categories")
public class SubscriptionCategoryController {

    private final SubscriptionCategoryRepository subscriptionCategoryRepository;

    SubscriptionCategoryController(SubscriptionCategoryRepository subscriptionCategoryRepository) {
        this.subscriptionCategoryRepository = subscriptionCategoryRepository;
    }

    @GetMapping("")
    public List<SubscriptionCategory> getCategories() {
        return this.subscriptionCategoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public SubscriptionCategory getCategory(@PathVariable Long id) {
        return this.subscriptionCategoryRepository.findById(id)
                .orElseThrow(() -> new SubscriptionCategoryNotFoundException(id));
    }
}
