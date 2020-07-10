package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.CommentPlayerDto;
import com.pa.asvblrapi.entity.CommentPlayer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {})
public interface CommentPlayerMapper extends EntityMapper<CommentPlayerDto, CommentPlayer> {
    CommentPlayerMapper instance = Mappers.getMapper(CommentPlayerMapper.class);

    @Mapping(source = "jersey.id", target = "idJersey")
    @Mapping(source = "match.id", target = "idMatch")
    CommentPlayerDto toDto(CommentPlayer commentPlayer);
}
