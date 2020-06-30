package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.PlayerDto;
import com.pa.asvblrapi.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PlayerMappingTests {
    @Test
    public void should_map_player_to_dto() {
        ClothingSize clothingSize = new ClothingSize((long)1,"L");
        SubscriptionCategory subscriptionCategory = new SubscriptionCategory((long)3, "test");
        User user = new User();
        user.setId((long)2);
        Player player = new Player();
        player.setId((long)4);
        player.setFirstName("firstName");
        player.setLastName("lastName");
        player.setGender(true);
        player.setAddress("address");
        player.setPostcode(75001);
        player.setCity("city");
        player.setEmail("email");
        player.setPhoneNumber("phoneNumber");
        player.setBirthDate(new Date());
        player.setLicenceNumber("licenceNumber");
        player.setTopSize(clothingSize);
        player.setPantsSize(clothingSize);
        player.setSubscriptionCategory(subscriptionCategory);
        player.setRequestedJerseyNumber(10);
        player.setUser(user);

        PlayerDto playerDto = PlayerMapper.instance.toDto(player);

        assertThat(playerDto).isNotNull();
        assertThat(playerDto.getId()).isEqualTo(player.getId());
        assertThat(playerDto.getFirstName()).isEqualTo(player.getFirstName());
        assertThat(playerDto.getLastName()).isEqualTo(player.getLastName());
        assertThat(playerDto.isGender()).isEqualTo(player.isGender());
        assertThat(playerDto.getAddress()).isEqualTo(player.getAddress());
        assertThat(playerDto.getPostcode()).isEqualTo(player.getPostcode());
        assertThat(playerDto.getCity()).isEqualTo(player.getCity());
        assertThat(playerDto.getEmail()).isEqualTo(player.getEmail());
        assertThat(playerDto.getPhoneNumber()).isEqualTo(player.getPhoneNumber());
        assertThat(playerDto.getBirthDate()).isEqualTo(player.getBirthDate());
        assertThat(playerDto.getLicenceNumber()).isEqualTo(player.getLicenceNumber());
        assertThat(playerDto.getIdTopSize()).isEqualTo(clothingSize.getId());
        assertThat(playerDto.getIdPantsSize()).isEqualTo(clothingSize.getId());
        assertThat(playerDto.getIdSubscriptionCategory()).isEqualTo(subscriptionCategory.getId());
        assertThat(playerDto.getRequestedJerseyNumber()).isEqualTo(player.getRequestedJerseyNumber());
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
