package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/players-city")
    public List<Object> getCountNbPlayersByCity() {
        return this.statisticsService.getCountNbPlayersByCity();
    }

    @GetMapping("/payments-mode")
    public List<Object> getCountNbPaymentMode() {
        return this.statisticsService.getCountNbPaymentMode();
    }
}
