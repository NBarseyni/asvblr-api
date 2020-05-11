package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.entity.ClothingSize;
import com.pa.asvblrapi.exception.ClothingSizeNotFoundException;
import com.pa.asvblrapi.repository.ClothingSizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(value = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/clothing-sizes")
public class ClothingSizeController {
    @Autowired
    private ClothingSizeRepository clothingSizeRepository;

    @GetMapping("")
    public List<ClothingSize> getClothingSizes() {
        return this.clothingSizeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ClothingSize getClothingSize(@PathVariable Long id) {
        return this.clothingSizeRepository.findById(id).
                orElseThrow(() -> new ClothingSizeNotFoundException(id));
    }
}
