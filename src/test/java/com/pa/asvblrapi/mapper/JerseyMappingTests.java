package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.JerseyDto;
import com.pa.asvblrapi.entity.Jersey;
import com.pa.asvblrapi.entity.Player;
import com.pa.asvblrapi.entity.Position;
import com.pa.asvblrapi.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JerseyMappingTests {

    @Test
    public void should_map_jersey_to_dto() {
        Position position = new Position();
        position.setId((long)2);
        Team team = new Team();
        team.setId((long)3);
        Player player = new Player();
        player.setId((long)4);
        Jersey jersey = new Jersey((long)1, 1, team, position, player);

        JerseyDto jerseyDto = JerseyMapper.instance.toDto(jersey);

        assertThat(jerseyDto).isNotNull();
        assertThat(jerseyDto.getId()).isEqualTo(jersey.getId());
        assertThat(jerseyDto.getNumber()).isEqualTo(jersey.getNumber());
        assertThat(jerseyDto.getIdTeam()).isEqualTo(jersey.getTeam().getId());
        assertThat(jerseyDto.getIdPosition()).isEqualTo(jersey.getPosition().getId());
        assertThat(jerseyDto.getIdPlayer()).isEqualTo(jersey.getPlayer().getId());
    }
}
