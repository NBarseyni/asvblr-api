package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.entity.PaymentMode;
import com.pa.asvblrapi.exception.PaymentModeNotFoundException;
import com.pa.asvblrapi.repository.PaymentModeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/paymentModes")
public class PaymentModeController {

    private final PaymentModeRepository paymentModeRepository;

    PaymentModeController(PaymentModeRepository paymentModeRepository) {
        this.paymentModeRepository = paymentModeRepository;
    }

    @GetMapping("/")
    public List<PaymentMode> getPaymentModes() {
        return paymentModeRepository.findAll();
    }

    @GetMapping("/{id}")
    public PaymentMode getPaymentMode(@PathVariable Long id) {
        return paymentModeRepository.findById(id)
                .orElseThrow(() -> new PaymentModeNotFoundException(id));
    }
}
