package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.*;
import com.pa.asvblrapi.entity.Match;
import com.pa.asvblrapi.entity.Photo;
import com.pa.asvblrapi.entity.Team;
import com.pa.asvblrapi.exception.*;
import com.pa.asvblrapi.mapper.MatchMapper;
import com.pa.asvblrapi.mapper.TeamMapper;
import com.pa.asvblrapi.service.MatchService;
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
    private final MatchService matchService;

    TeamController(TeamService teamService, PhotoService photoService, MatchService matchService) {
        this.teamService = teamService;
        this.photoService = photoService;
        this.matchService = matchService;
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

    @GetMapping("/list-detail")
    public List<TeamListDto> getTeamList() {
        return this.teamService.getTeamList();
    }

    @GetMapping("/{id}/add-player")
    public ResponseEntity<Object> getPlayersNotInTeam(@PathVariable Long id) {
        try {
            List<PlayerDto> players = this.teamService.getPlayersNotInTeam(id);
            return ResponseEntity.status(HttpStatus.OK).body(players);
        } catch (TeamNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/matches")
    public List<MatchDto> getMatchesByIdTeam(@PathVariable Long id) {
        return this.matchService.getAllMatchesByIdTeam(id);
    }

    @PostMapping("")
    public ResponseEntity<Object> createTeam(@Valid @RequestBody TeamDto teamDto) {
        try {
            TeamDto team = this.teamService.createTeam(teamDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(team);
        }
        catch (SeasonNotFoundException | TeamCategoryNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
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
            TeamDto team = this.teamService.updateTeam(id, teamDto);
            return ResponseEntity.status(HttpStatus.OK).body(team);
        }
        catch (SeasonNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{id}/coach/{idCoach}")
    public ResponseEntity<Object> setCoach(@PathVariable Long id, @PathVariable Long idCoach) {
        try {
            TeamDto teamDto = this.teamService.setCoach(id, idCoach);
            return ResponseEntity.status(HttpStatus.OK).body(teamDto);
        } catch (TeamNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/leader/{idLeader}")
    public ResponseEntity<Object> setLeader(@PathVariable Long id, @PathVariable Long idLeader) {
        try {
            TeamDto teamDto = this.teamService.setLeader(id, idLeader);
            return ResponseEntity.status(HttpStatus.OK).body(teamDto);
        } catch (TeamNotFoundException | PlayerNotFoundException | JerseyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/players")
    public ResponseEntity<Object> getPlayers(@PathVariable Long id) {
        try {
            List<TeamPlayerDto> teamPlayerDto = this.teamService.getTeamPlayers(id);
            return ResponseEntity.status(HttpStatus.OK).body(teamPlayerDto);
        } catch (TeamNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @PostMapping("/{id}/players")
    public ResponseEntity<Object> addPlayer(@PathVariable Long id, @Valid @RequestBody List<AddPlayerTeamDto> dtos) {
        try {
            for (AddPlayerTeamDto dto : dtos) {
                this.teamService.addPlayer(id, dto);
            }
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{idTeam}/players/{idPlayer}")
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
