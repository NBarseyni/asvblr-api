package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.*;
import com.pa.asvblrapi.entity.Position;
import com.pa.asvblrapi.exception.*;
import com.pa.asvblrapi.mapper.TeamMapper;
import com.pa.asvblrapi.service.MatchService;
import com.pa.asvblrapi.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/teams")
public class TeamController {

    private final TeamService teamService;
    private final MatchService matchService;

    TeamController(TeamService teamService, MatchService matchService) {
        this.teamService = teamService;
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

    @GetMapping("/{id}/matches")
    public List<MatchDto> getMatchesByIdTeam(@PathVariable Long id) {
        return this.matchService.getAllMatchesByIdTeam(id);
    }

    @PostMapping("")
    public ResponseEntity<Object> createTeam(@Valid @RequestBody TeamDto teamDto) {
        try {
            TeamDto team = this.teamService.createTeam(teamDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(team);
        } catch (SeasonNotFoundException | TeamCategoryNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTeam(@PathVariable Long id, @Valid @RequestBody TeamDto teamDto) {
        try {
            TeamDto team = this.teamService.updateTeam(id, teamDto);
            return ResponseEntity.status(HttpStatus.OK).body(team);
        } catch (SeasonNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/photo")
    public ResponseEntity<Object> setPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            TeamDto teamDto = this.teamService.setPhoto(id, file);
            return ResponseEntity.status(HttpStatus.OK).body(teamDto);
        } catch (TeamNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/coach")
    public ResponseEntity<Object> setCoach(@PathVariable Long id, @RequestBody CoachDto coachDto) {
        try {
            TeamDto teamDto = this.teamService.setCoach(id, coachDto.getIdCoach());
            return ResponseEntity.status(HttpStatus.OK).body(teamDto);
        } catch (TeamNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/leader")
    public ResponseEntity<Object> setLeader(@PathVariable Long id, @RequestBody LeaderDto leaderDto) {
        try {
            TeamDto teamDto = this.teamService.setLeader(id, leaderDto.getIdLeader());
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

    @GetMapping("/{id}/users")
    public ResponseEntity<Object> getUsers(@PathVariable Long id) {
        try {
            List<UserDto> users = this.teamService.getTeamUsers(id);
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (TeamNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/players")
    public ResponseEntity<Object> addPlayer(@PathVariable Long id, @Valid @RequestBody List<AddPlayerTeamDto> dtos) {
        try {
            List<TeamPlayerDto> teamPlayersDto = new ArrayList<>();
            for (AddPlayerTeamDto dto : dtos) {
                teamPlayersDto.add(this.teamService.addPlayer(id, dto));
            }
            return ResponseEntity.status(HttpStatus.OK).body(teamPlayersDto);
        } catch (TeamNotFoundException | PlayerNotFoundException | PositionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{idTeam}/players/{idPlayer}")
    public ResponseEntity<Object> updatePositionAndNumberPlayer(@PathVariable Long idTeam,
                                                                @PathVariable Long idPlayer,
                                                                @Valid @RequestBody UpdatePlayerTeamDto dto) {
        try {
            TeamPlayerDto teamPlayerDto = this.teamService.updatePlayer(idTeam, idPlayer, dto);
            return ResponseEntity.status(HttpStatus.OK).body(teamPlayerDto);
        } catch (TeamNotFoundException | PlayerNotFoundException | JerseyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idTeam}/players")
    public ResponseEntity<Object> removePlayer(@PathVariable Long idTeam, @RequestBody List<Long> idsPlayer) {
        try {
            for (Long idPlayer : idsPlayer) {
                this.teamService.removePlayer(idTeam, idPlayer);
            }
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
        } catch (TeamNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
