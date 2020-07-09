package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.MatchDto;
import com.pa.asvblrapi.entity.Match;
import com.pa.asvblrapi.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MatchMappingTests {

    @Test
    public void should_map_match_to_dto() {
        Team team = new Team();
        team.setId((long)2);
        Match match = new Match((long)1, new Date(), "place", true, "oppositeTeam",
                "comment", 10, 9, 8, 7,
                6, team, new ArrayList<>(), new ArrayList<>());

        MatchDto matchDto = MatchMapper.instance.toDto(match);

        assertThat(matchDto).isNotNull();
        assertThat(matchDto.getId()).isEqualTo(match.getId());
        assertThat(matchDto.getDate()).isEqualTo(match.getDate());
        assertThat(matchDto.getPlace()).isEqualTo(match.getPlace());
        assertThat(matchDto.isType()).isEqualTo(match.isType());
        assertThat(matchDto.getOppositeTeam()).isEqualTo(match.getOppositeTeam());
        assertThat(matchDto.getComment()).isEqualTo(match.getComment());
        assertThat(matchDto.getTechnicalRating()).isEqualTo(match.getTechnicalRating());
        assertThat(matchDto.getCollectiveRating()).isEqualTo(match.getCollectiveRating());
        assertThat(matchDto.getOffensiveRating()).isEqualTo(match.getOffensiveRating());
        assertThat(matchDto.getDefensiveRating()).isEqualTo(match.getDefensiveRating());
        assertThat(matchDto.getCombativenessRating()).isEqualTo(match.getCombativenessRating());
        assertThat(matchDto.getIdTeam()).isEqualTo(match.getTeam().getId());
    }
}
