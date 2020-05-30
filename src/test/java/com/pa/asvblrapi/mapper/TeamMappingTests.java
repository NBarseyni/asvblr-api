package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.TeamDto;
import com.pa.asvblrapi.entity.Season;
import com.pa.asvblrapi.entity.Team;
import com.pa.asvblrapi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TeamMappingTests {
    @Test
    public void should_map_team_to_dto() {
        Season season = new Season();
        season.setId((long)1);
        User coach = new User();
        coach.setId((long)3);
        Team team = new Team();
        team.setId((long)2);
        team.setName("team");
        team.setSeason(season);
        team.setCoach(coach);

        TeamDto teamDto = TeamMapper.instance.toDto(team);

        assertThat(teamDto).isNotNull();
        assertThat(teamDto.getId()).isEqualTo(team.getId());
        assertThat(teamDto.getName()).isEqualTo(team.getName());
        assertThat(teamDto.getIdSeason()).isEqualTo(season.getId());
        assertThat(teamDto.getIdCoach()).isEqualTo(coach.getId());
    }
}
