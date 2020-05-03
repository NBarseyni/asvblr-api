package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.PlayerDto;
import com.pa.asvblrapi.entity.Player;
import com.pa.asvblrapi.exception.PlayerNotFoundException;
import com.pa.asvblrapi.exception.SeasonNotFoundException;
import com.pa.asvblrapi.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    private final PlayerService playerService;

    PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/")
    public List<Player> getPlayers() {
        return this.playerService.getAllPlayer();
    }

    @GetMapping("/{id}")
    public Player getPlayer(@PathVariable Long id) {
        return this.playerService.getPlayer(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updatePlayer(@PathVariable Long id, @Valid @RequestBody PlayerDto playerDto) {
        try {
            Player player = this.playerService.updatePlayer(id, playerDto);
            return ResponseEntity.status(HttpStatus.OK).body(player);
        }
        catch (PlayerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
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
