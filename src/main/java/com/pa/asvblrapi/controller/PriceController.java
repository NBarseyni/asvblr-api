package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.entity.Price;
import com.pa.asvblrapi.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/prices")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @GetMapping("")
    public List<Price> getAllPrice() {
        return this.priceService.getAllPrice();
    }

    @GetMapping("/{id}")
    public Price getPrice(@PathVariable Long id) {
        return this.priceService.getPriceById(id);
    }

    @GetMapping("/{code}")
    public Price getByCode(@PathVariable String code) {
        return this.priceService.getPriceByCode(code);
    }

    @PatchMapping("")
    public List<Price> updatePrice(@Valid @RequestBody List<Price> prices) {
        return this.priceService.updatePrice(prices);
    }

}
