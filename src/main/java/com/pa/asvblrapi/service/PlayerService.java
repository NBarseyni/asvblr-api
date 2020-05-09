package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.PlayerDto;
import com.pa.asvblrapi.entity.Player;
import com.pa.asvblrapi.entity.Subscription;
import com.pa.asvblrapi.entity.User;
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

    public Player createPlayer(Subscription subscription, User user) {
        Player player = new Player(
                subscription.getFirstName(),
                subscription.getLastName(),
                subscription.getAddress(),
                subscription.getPostcode(),
                subscription.getCity(),
                subscription.getEmail(),
                subscription.getPhoneNumber(),
                subscription.getBirthDate(),
                subscription.getTopSize(),
                subscription.getPantsSize(),
                user
        );
        return this.playerRepository.save(player);
    }

    public Player updatePlayer(Long id, PlayerDto playerDto) throws PlayerNotFoundException {
        Optional<Player> player = this.playerRepository.findById(id);
        if (!player.isPresent()) {
            throw new PlayerNotFoundException(id);
        }
        player.get().setFirstName(playerDto.getFirstName());
        player.get().setLastName(playerDto.getLastName());
        player.get().setAddress(playerDto.getAddress());
        player.get().setPostcode(playerDto.getPostcode());
        player.get().setCity(playerDto.getCity());
        player.get().setEmail(playerDto.getEmail());
        player.get().setPhoneNumber(playerDto.getPhoneNumber());
        player.get().setBirthDate(playerDto.getBirthDate());
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
