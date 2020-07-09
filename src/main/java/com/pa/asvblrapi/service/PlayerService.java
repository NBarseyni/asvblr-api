package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.PlayerDto;
import com.pa.asvblrapi.dto.SubscriptionDto;
import com.pa.asvblrapi.entity.*;
import com.pa.asvblrapi.exception.ClothingSizeNotFoundException;
import com.pa.asvblrapi.exception.PlayerNotFoundException;
import com.pa.asvblrapi.exception.SubscriptionCategoryNotFoundException;
import com.pa.asvblrapi.exception.UserNotFoundException;
import com.pa.asvblrapi.mapper.PlayerMapper;
import com.pa.asvblrapi.mapper.SubscriptionMapper;
import com.pa.asvblrapi.repository.ClothingSizeRepository;
import com.pa.asvblrapi.repository.PlayerRepository;
import com.pa.asvblrapi.repository.SubscriptionCategoryRepository;
import com.pa.asvblrapi.repository.UserRepository;
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

    @Autowired
    private SubscriptionCategoryRepository subscriptionCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Player> getAllPlayer() {
        return this.playerRepository.findAll();
    }

    public Optional<Player> getPlayer(Long id) {
        return this.playerRepository.findById(id);
    }

    public PlayerDto getPlayerByIdUser(Long id) throws UserNotFoundException, PlayerNotFoundException {
        Optional<User> user = this.userRepository.findById(id);
        if (!user.isPresent()) {
            throw new UserNotFoundException(id);
        }
        if (user.get().getPlayer() == null) {
            throw new PlayerNotFoundException(id, 1);
        }
        return PlayerMapper.instance.toDto(user.get().getPlayer());
    }

    public Subscription getLastSubscription(Long id) throws PlayerNotFoundException {
        Optional<Player> player = this.playerRepository.findById(id);
        if (!player.isPresent()) {
            throw new PlayerNotFoundException(id);
        }
        return player.get().getSubscriptions().get(player.get().getSubscriptions().size() - 1);
    }

    public Player createPlayer(Subscription subscription, User user) {
        Player player = new Player(
                subscription.getFirstName(),
                subscription.getLastName(),
                subscription.isGender(),
                subscription.getAddress(),
                subscription.getPostcode(),
                subscription.getCity(),
                subscription.getEmail(),
                subscription.getPhoneNumber(),
                subscription.getBirthDate(),
                subscription.getTopSize(),
                subscription.getPantsSize(),
                subscription.getSubscriptionCategory(),
                subscription.getRequestedJerseyNumber(),
                user
        );
        user.setPlayer(player);
        return this.playerRepository.save(player);
    }

    public Player updatePlayer(Long id, PlayerDto playerDto) throws PlayerNotFoundException, ClothingSizeNotFoundException {
        Optional<Player> player = this.playerRepository.findById(id);
        if (!player.isPresent()) {
            throw new PlayerNotFoundException(id);
        }
        if (playerDto.getIdTopSize() != null) {
            Optional<ClothingSize> topSize = this.clothingSizeRepository.findById(playerDto.getIdTopSize());
            if (!topSize.isPresent()) {
                throw new ClothingSizeNotFoundException(playerDto.getIdTopSize());
            }
            player.get().setTopSize(topSize.get());
        }
        if (playerDto.getIdPantsSize() != null) {
            Optional<ClothingSize> pantsSize = this.clothingSizeRepository.findById(playerDto.getIdPantsSize());
            if (!pantsSize.isPresent()) {
                throw new ClothingSizeNotFoundException(playerDto.getIdPantsSize());
            }
            player.get().setPantsSize(pantsSize.get());
        }
        Optional<SubscriptionCategory> subscriptionCategory = this.subscriptionCategoryRepository.findById(playerDto.getIdSubscriptionCategory());
        if (!subscriptionCategory.isPresent()) {
            throw new SubscriptionCategoryNotFoundException(playerDto.getIdSubscriptionCategory());
        }
        player.get().setFirstName(playerDto.getFirstName());
        player.get().setLastName(playerDto.getLastName());
        player.get().setAddress(playerDto.getAddress());
        player.get().setPostcode(playerDto.getPostcode());
        player.get().setCity(playerDto.getCity());
        player.get().setEmail(playerDto.getEmail());
        player.get().setPhoneNumber(playerDto.getPhoneNumber());
        player.get().setBirthDate(playerDto.getBirthDate());
        player.get().setSubscriptionCategory(subscriptionCategory.get());
        return this.playerRepository.save(player.get());
    }

    public PlayerDto updateLicenceNumberPlayer(Long id, String licenceNumber) throws PlayerNotFoundException {
        Optional<Player> player = this.playerRepository.findById(id);
        if (!player.isPresent()) {
            throw new PlayerNotFoundException(id);
        }
        player.get().setLicenceNumber(licenceNumber);
        return PlayerMapper.instance.toDto(this.playerRepository.save(player.get()));
    }

    public void deletePlayer(Long id) throws PlayerNotFoundException, AccessDeniedException {
        Optional<Player> player = this.playerRepository.findById(id);

        if (!player.isPresent()) {
            throw new PlayerNotFoundException(id);
        }
        this.playerRepository.delete(player.get());
    }
}
