package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.SeasonDto;
import com.pa.asvblrapi.dto.SubscriptionDto;
import com.pa.asvblrapi.entity.Season;
import com.pa.asvblrapi.entity.Subscription;
import com.pa.asvblrapi.exception.SeasonNotFoundException;
import com.pa.asvblrapi.mapper.SeasonMapper;
import com.pa.asvblrapi.mapper.SubscriptionMapper;
import com.pa.asvblrapi.service.SeasonService;
import com.pa.asvblrapi.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/seasons")
public class SeasonController {

    private final SeasonService seasonService;
    private final SubscriptionService subscriptionService;

    SeasonController(SeasonService seasonService, SubscriptionService subscriptionService) {
        this.seasonService = seasonService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("")
    public List<SeasonDto> getSeasons() {
        return SeasonMapper.instance.toDto(this.seasonService.getAllSeasons());
    }

    @GetMapping("/{id}")
    public SeasonDto getSeason(@PathVariable Long id) {
        return SeasonMapper.instance.toDto(this.seasonService.getSeason(id)
                .orElseThrow(() -> new SeasonNotFoundException(id)));
    }

    @GetMapping("/current-season")
    public SeasonDto getCurrentSeason() {
        return SeasonMapper.instance.toDto(this.seasonService.getCurrentSeason()
        .orElseThrow(RuntimeException::new));
    }

    @PostMapping("")
    public ResponseEntity<SeasonDto> createSeason(@Valid @RequestBody SeasonDto seasonDto) {
        try {
            Season season = this.seasonService.createSeason(seasonDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(SeasonMapper.instance.toDto(season));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSeason(@PathVariable Long id, @Valid @RequestBody SeasonDto seasonDto) {
        try {
            Season season = this.seasonService.updateSeason(id, seasonDto);
            return ResponseEntity.status(HttpStatus.OK).body(SeasonMapper.instance.toDto(season));
        }
        catch (SeasonNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
