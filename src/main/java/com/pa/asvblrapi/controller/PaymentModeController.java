package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.entity.PaymentMode;
import com.pa.asvblrapi.exception.PaymentModeNotFoundException;
import com.pa.asvblrapi.repository.PaymentModeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/payment-modes")
public class PaymentModeController {

    private final PaymentModeRepository paymentModeRepository;

    PaymentModeController(PaymentModeRepository paymentModeRepository) {
        this.paymentModeRepository = paymentModeRepository;
    }

    @GetMapping("")
    public List<PaymentMode> getPaymentModes() {
        return paymentModeRepository.findAll();
    }

    @GetMapping("/{id}")
    public PaymentMode getPaymentMode(@PathVariable Long id) {
        return paymentModeRepository.findById(id)
                .orElseThrow(() -> new PaymentModeNotFoundException(id));
    }
}
