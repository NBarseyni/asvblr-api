package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.SeasonDto;
import com.pa.asvblrapi.entity.Season;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SeasonMappingTests {
    @Test
    public void should_map_season_to_dto() {
        Season season = new Season();
        season.setId((long)1);
        season.setName("2019-2020");

        SeasonDto seasonDto = SeasonMapper.instance.toDto(season);

        assertThat(seasonDto).isNotNull();
        assertThat(seasonDto.getId()).isEqualTo(season.getId());
        assertThat(seasonDto.getName()).isEqualTo(season.getName());
    }
}
