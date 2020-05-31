package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.CommentPlayerDto;
import com.pa.asvblrapi.entity.CommentPlayer;
import com.pa.asvblrapi.entity.Jersey;
import com.pa.asvblrapi.entity.Match;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CommentPlayerMappingTests {

    @Test
    public void should_map_commentPlayer_to_dto() {
        Jersey jersey = new Jersey();
        jersey.setId((long)1);
        Match match = new Match();
        match.setId((long)2);
        CommentPlayer commentPlayer = new CommentPlayer((long)3, "comment", 20, jersey, match);

        CommentPlayerDto commentPlayerDto = CommentPlayerMapper.instance.toDto(commentPlayer);

        assertThat(commentPlayerDto).isNotNull();
        assertThat(commentPlayerDto.getId()).isEqualTo(commentPlayer.getId());
        assertThat(commentPlayerDto.getComment()).isEqualTo(commentPlayerDto.getComment());
        assertThat(commentPlayerDto.getRate()).isEqualTo(commentPlayerDto.getRate());
        assertThat(commentPlayerDto.getIdJersey()).isEqualTo(jersey.getId());
        assertThat(commentPlayerDto.getIdMatch()).isEqualTo(match.getId());
    }
}
