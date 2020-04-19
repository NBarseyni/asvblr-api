package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.TeamDto;
import com.pa.asvblrapi.entity.Team;
import com.pa.asvblrapi.exception.SeasonNotFoundException;
import com.pa.asvblrapi.exception.TeamNotFoundException;
import com.pa.asvblrapi.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/teams")
public class TeamController {

    private final TeamService teamService;

    TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/")
    public List<Team> getTeams() {
        return this.teamService.getAllTeam();
    }

    @GetMapping("/{id}")
    public Team getTeam(@PathVariable Long id) {
        return this.teamService.getTeam(id)
                .orElseThrow(() -> new TeamNotFoundException(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Team> create(@Valid @RequestBody TeamDto teamDto) {
        try {
            Team team = this.teamService.createTeam(teamDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(team);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody TeamDto teamDto) {
        try {
            Team team = this.teamService.updateTeam(id, teamDto);
            return ResponseEntity.status(HttpStatus.OK).body(team);
        }
        catch (SeasonNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            this.teamService.deleteTeam(id);
        }
        catch (TeamNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
