package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.AddPlayerTeamDto;
import com.pa.asvblrapi.dto.TeamDto;
import com.pa.asvblrapi.entity.Photo;
import com.pa.asvblrapi.entity.Team;
import com.pa.asvblrapi.exception.*;
import com.pa.asvblrapi.mapper.TeamMapper;
import com.pa.asvblrapi.service.PhotoService;
import com.pa.asvblrapi.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/teams")
public class TeamController {

    private final TeamService teamService;
    private final PhotoService photoService;

    TeamController(TeamService teamService, PhotoService photoService) {
        this.teamService = teamService;
        this.photoService = photoService;
    }

    @GetMapping("")
    public List<TeamDto> getTeams() {
        return TeamMapper.instance.toDto(this.teamService.getAllTeam());
    }

    @GetMapping("/{id}")
    public TeamDto getTeam(@PathVariable Long id) {
        return TeamMapper.instance.toDto(this.teamService.getTeam(id)
                .orElseThrow(() -> new TeamNotFoundException(id)));
    }

    @PostMapping("")
    public ResponseEntity<TeamDto> createTeam(@Valid @RequestBody TeamDto teamDto) {
        try {
            Team team = this.teamService.createTeam(teamDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(TeamMapper.instance.toDto(team));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{id}/photos")
    public ResponseEntity<Object> createTeamPhoto(@RequestParam("file") MultipartFile multipartFile, @PathVariable Long id) {
        Team team = this.teamService.getTeam(id)
                .orElseThrow(() -> new TeamNotFoundException(id));
        try {
            Photo photo = this.photoService.createTeamPhoto(multipartFile, team);
            return ResponseEntity.status(HttpStatus.CREATED).body(photo);
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTeam(@PathVariable Long id, @Valid @RequestBody TeamDto teamDto) {
        try {
            Team team = this.teamService.updateTeam(id, teamDto);
            return ResponseEntity.status(HttpStatus.OK).body(TeamMapper.instance.toDto(team));
        }
        catch (SeasonNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/{id}/coach/{idCoach}")
    public ResponseEntity<Object> addCoach(@PathVariable Long id, @PathVariable Long idCoach) {
        try {
            this.teamService.addCoach(id, idCoach);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (TeamNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PatchMapping("/{id}/remove-coach")
    public ResponseEntity<Object> removeCoach(@PathVariable Long id) {
        try {
            this.teamService.removeCoach(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (TeamNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PostMapping("/{id}/add-player")
    public ResponseEntity<Object> addPlayer(@PathVariable Long id, @Valid @RequestBody AddPlayerTeamDto dto) {
        try {
            this.teamService.addPlayer(id, dto);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{idTeam}/remove-player/{idPlayer}")
    public ResponseEntity<Object> removePlayer(@PathVariable Long idTeam, @PathVariable Long idPlayer) {
        try {
            this.teamService.removePlayer(idTeam, idPlayer);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (TeamNotFoundException | PlayerNotFoundException | JerseyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTeam(@PathVariable Long id) {
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
