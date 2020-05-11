package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.PlayerDto;
import com.pa.asvblrapi.entity.ClothingSize;
import com.pa.asvblrapi.entity.Player;
import com.pa.asvblrapi.entity.Subscription;
import com.pa.asvblrapi.entity.User;
import com.pa.asvblrapi.exception.ClothingSizeNotFoundException;
import com.pa.asvblrapi.exception.PlayerNotFoundException;
import com.pa.asvblrapi.repository.ClothingSizeRepository;
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

    @Autowired
    private ClothingSizeRepository clothingSizeRepository;

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

    public Player updatePlayer(Long id, PlayerDto playerDto) throws PlayerNotFoundException, ClothingSizeNotFoundException {
        Optional<Player> player = this.playerRepository.findById(id);
        if (!player.isPresent()) {
            throw new PlayerNotFoundException(id);
        }
        Optional<ClothingSize> topSize = this.clothingSizeRepository.findById(playerDto.getIdTopSize());
        if (!topSize.isPresent()) {
            throw new ClothingSizeNotFoundException(id);
        }
        Optional<ClothingSize> pantsSize = this.clothingSizeRepository.findById(playerDto.getIdPantsSize());
        if (!pantsSize.isPresent()) {
            throw new ClothingSizeNotFoundException(id);
        }
        player.get().setFirstName(playerDto.getFirstName());
        player.get().setLastName(playerDto.getLastName());
        player.get().setAddress(playerDto.getAddress());
        player.get().setPostcode(playerDto.getPostcode());
        player.get().setCity(playerDto.getCity());
        player.get().setEmail(playerDto.getEmail());
        player.get().setPhoneNumber(playerDto.getPhoneNumber());
        player.get().setBirthDate(playerDto.getBirthDate());
        player.get().setTopSize(topSize.get());
        player.get().setPantsSize(pantsSize.get());
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
