package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.PlayerDto;
import com.pa.asvblrapi.entity.Player;
import com.pa.asvblrapi.exception.PlayerNotFoundException;
import com.pa.asvblrapi.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> getAllPlayer() {
        return this.playerRepository.findAll();
    }

    public Optional<Player> getPlayer(Long id) {
        return this.playerRepository.findById(id);
    }

    public Player updatePlayer(Long id, PlayerDto playerDto) throws PlayerNotFoundException {
        Optional<Player> player = this.playerRepository.findById(id);
        if (!player.isPresent()) {
            throw new PlayerNotFoundException(id);
        }
        player.get().setFirstName(playerDto.getFirstName());
        player.get().setLastName(playerDto.getLastName());
        player.get().setEmail(playerDto.getEmail());
        player.get().setPhoneNumber(playerDto.getPhoneNumber());
        player.get().setJerseySize(playerDto.getJerseySize());
        player.get().setPantSize(playerDto.getPantSize());
        return this.playerRepository.save(player.get());
    }

    public void deletePlayer(Long id) throws PlayerNotFoundException, AccessDeniedException {
        Optional<Player> player = this.playerRepository.findById(id);

        if(!player.isPresent()) {
            throw new PlayerNotFoundException(id);
        }
        this.playerRepository.delete(player.get());
    }
}
