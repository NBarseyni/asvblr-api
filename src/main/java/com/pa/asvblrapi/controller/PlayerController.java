package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.PlayerDto;
import com.pa.asvblrapi.dto.SubscriptionDto;
import com.pa.asvblrapi.dto.TeamDto;
import com.pa.asvblrapi.entity.Player;
import com.pa.asvblrapi.exception.PlayerNotFoundException;
import com.pa.asvblrapi.exception.SeasonNotFoundException;
import com.pa.asvblrapi.mapper.PlayerMapper;
import com.pa.asvblrapi.mapper.SubscriptionMapper;
import com.pa.asvblrapi.service.PlayerService;
import com.pa.asvblrapi.service.SubscriptionService;
import com.pa.asvblrapi.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private TeamService teamService;

    @GetMapping("")
    public List<PlayerDto> getPlayers() {
        return PlayerMapper.instance.toDto(this.playerService.getAllPlayer());
    }

    @GetMapping("/{id}")
    public PlayerDto getPlayer(@PathVariable Long id) {
        return PlayerMapper.instance.toDto(this.playerService.getPlayer(id)
                .orElseThrow(() -> new PlayerNotFoundException(id)));
    }

    @GetMapping("/{id}/teams")
    public ResponseEntity<Object> getTeamByPlayer(@PathVariable Long id) {
        try {
            List<TeamDto> teams = teamService.getTeamsByPlayer(id);
            return ResponseEntity.status(HttpStatus.OK).body(teams);
        } catch (PlayerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/subscriptions")
    public List<SubscriptionDto> getSubscriptionsByPlayer(@PathVariable Long id) {
        return SubscriptionMapper.instance.toDto(this.subscriptionService.getSubscriptionsByPlayer(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePlayer(@PathVariable Long id, @Valid @RequestBody PlayerDto playerDto) {
        try {
            Player player = this.playerService.updatePlayer(id, playerDto);
            return ResponseEntity.status(HttpStatus.OK).body(PlayerMapper.instance.toDto(player));
        }
        catch (PlayerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePlayer(@PathVariable Long id) {
        try {
            this.playerService.deletePlayer(id);
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
