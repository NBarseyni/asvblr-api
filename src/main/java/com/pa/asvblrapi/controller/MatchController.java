package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.CommentMatchDto;
import com.pa.asvblrapi.dto.DriveDto;
import com.pa.asvblrapi.dto.MatchDto;
import com.pa.asvblrapi.exception.MatchNotFoundException;
import com.pa.asvblrapi.exception.TeamNotFoundException;
import com.pa.asvblrapi.service.DriveService;
import com.pa.asvblrapi.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/matches")
public class MatchController {
    @Autowired
    private MatchService matchService;

    @Autowired
    private DriveService driveService;

    @GetMapping("")
    public List<MatchDto> getMatches() {
        return this.matchService.getAllMatches();
    }

    @GetMapping("/{id}")
    public MatchDto getMatch(@PathVariable Long id) {
        return this.matchService.getMatch(id);
    }

    @GetMapping("/{id}/drives")
    public ResponseEntity<Object> getDrives(@PathVariable Long id) {
        try {
            List<DriveDto> drives = this.driveService.getAllByIdMatch(id);
            return ResponseEntity.status(HttpStatus.OK).body(drives);
        } catch (MatchNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<Object> createMatch(@Valid @RequestBody MatchDto matchDto) {
        try {
            MatchDto match = this.matchService.createMatch(matchDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(match);
        } catch (TeamNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMatch(@PathVariable Long id, @Valid @RequestBody MatchDto matchDto) {
        try {
            MatchDto match = this.matchService.updateMatch(id, matchDto);
            return ResponseEntity.status(HttpStatus.OK).body(match);
        } catch (MatchNotFoundException | TeamNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> commentMatch(@PathVariable Long id, @Valid @RequestBody CommentMatchDto commentMatchDto) {
        try {
            MatchDto matchDto = this.matchService.commentMatch(id, commentMatchDto);
            return ResponseEntity.status(HttpStatus.OK).body(matchDto);
        } catch (MatchNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMatch(@PathVariable Long id) {
        try {
            this.matchService.deleteMatch(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (MatchNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
