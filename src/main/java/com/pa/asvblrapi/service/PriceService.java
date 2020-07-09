package com.pa.asvblrapi.service;

import com.pa.asvblrapi.entity.Price;
import com.pa.asvblrapi.exception.PriceNotFoundException;
import com.pa.asvblrapi.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    public List<Price> getAllPrice() {
        return this.priceRepository.findAll();
    }

    public Price getPriceById(Long id) {
        return this.priceRepository.findById(id)
                .orElseThrow(() -> new PriceNotFoundException(id));
    }

    public Price getPriceByCode(String code) {
        return this.priceRepository.findByCode(code)
                .orElseThrow(() -> new PriceNotFoundException(code));
    }

    public List<Price> updatePrice(List<Price> prices) {
        return this.priceRepository.saveAll(prices);
    }
}
