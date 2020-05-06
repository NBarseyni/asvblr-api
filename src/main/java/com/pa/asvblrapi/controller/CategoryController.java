package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.entity.Category;
import com.pa.asvblrapi.exception.CategoryNotFoundException;
import com.pa.asvblrapi.repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/")
    public List<Category> getCategories() {
        return this.categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id) {
        return this.categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }
}
