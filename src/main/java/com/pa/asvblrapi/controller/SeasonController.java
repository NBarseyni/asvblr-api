package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.SeasonDto;
import com.pa.asvblrapi.entity.Season;
import com.pa.asvblrapi.exception.SeasonNotFoundException;
import com.pa.asvblrapi.service.SeasonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/seasons")
public class SeasonController {

    private final SeasonService seasonService;

    SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @GetMapping("/")
    public List<Season> getSeasons() {
        return this.seasonService.getAllSeasons();
    }

    @GetMapping("/{id}")
    public Season getSeason(@PathVariable Long id) {
        return this.seasonService.getSeason(id)
                .orElseThrow(() -> new SeasonNotFoundException(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Season> create(@Valid @RequestBody SeasonDto seasonDto) {
        try {
            Season season = this.seasonService.createSeason(seasonDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(season);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /*
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody SeasonDto seasonDto) {

    }

     */

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            this.seasonService.deleteSeason(id);
        }
        catch (SeasonNotFoundException e) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
