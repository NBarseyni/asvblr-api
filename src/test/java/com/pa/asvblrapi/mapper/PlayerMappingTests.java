package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.PlayerDto;
import com.pa.asvblrapi.entity.ClothingSize;
import com.pa.asvblrapi.entity.Jersey;
import com.pa.asvblrapi.entity.Player;
import com.pa.asvblrapi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PlayerMappingTests {
    @Test
    public void should_map_player_to_dto() {
        ClothingSize clothingSize = new ClothingSize((long)1,"L");
        User user = new User();
        user.setId((long)2);
        Jersey jersey = new Jersey();
        jersey.setId((long)3);
        Player player = new Player();
        player.setId((long)4);
        player.setFirstName("firstName");
        player.setLastName("lastName");
        player.setAddress("address");
        player.setPostcode(75001);
        player.setCity("city");
        player.setEmail("email");
        player.setPhoneNumber("phoneNumber");
        player.setBirthDate(new Date());
        player.setLicenceNumber("licenceNumber");
        player.setTopSize(clothingSize);
        player.setPantsSize(clothingSize);
        player.setUser(user);
        player.setJersey(jersey);

        PlayerDto playerDto = PlayerMapper.instance.toDto(player);

        assertThat(playerDto).isNotNull();
        assertThat(playerDto.getId()).isEqualTo(player.getId());
        assertThat(playerDto.getFirstName()).isEqualTo(player.getFirstName());
        assertThat(playerDto.getLastName()).isEqualTo(player.getLastName());
        assertThat(playerDto.getAddress()).isEqualTo(player.getAddress());
        assertThat(playerDto.getPostcode()).isEqualTo(player.getPostcode());
        assertThat(playerDto.getCity()).isEqualTo(player.getCity());
        assertThat(playerDto.getEmail()).isEqualTo(player.getEmail());
        assertThat(playerDto.getPhoneNumber()).isEqualTo(player.getPhoneNumber());
        assertThat(playerDto.getBirthDate()).isEqualTo(player.getBirthDate());
        assertThat(playerDto.getLicenceNumber()).isEqualTo(player.getLicenceNumber());
        assertThat(playerDto.getIdTopSize()).isEqualTo(clothingSize.getId());
        assertThat(playerDto.getIdPantsSize()).isEqualTo(clothingSize.getId());
        assertThat(playerDto.getIdUser()).isEqualTo(player.getUser().getId());
        assertThat(playerDto.getIdJersey()).isEqualTo(player.getJersey().getId());
    }

    /*
    @Test
    public void should_map_dto_to_player() {
        PlayerDto playerDto = new PlayerDto((long)1, "firstName", "lastName", "address",
                75001, "city", "email", "phoneNumber", new Date(), "",
                "L", "L", (long)2, (long)3);

        Player player = PlayerMapper.instance.toEntity(playerDto);

        assertThat(player).isNotNull();
        assertThat(player.getId()).isEqualTo(1);
        assertThat(player.getUser().getId()).isEqualTo(2);
        assertThat(player.getJersey().getId()).isEqualTo(3);
    }
     */
}