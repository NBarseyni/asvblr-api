package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.VisitDto;
import com.pa.asvblrapi.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/players-by-age")
    public List<Object> getNbPlayersByAgeByTrancheOf5() {
        return this.statisticsService.getNbPlayersByAgeByTrancheOf5();
    }

    @GetMapping("/visits")
    public List<Object> getVisitStat() {
        return this.statisticsService.getVisitStat();
    }

    @PostMapping("/visits")
    public ResponseEntity<Object> createStatVisit(@Valid @RequestBody VisitDto dto) {
        try {
            this.statisticsService.createVisit(dto);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
