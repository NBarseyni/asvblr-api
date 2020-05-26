package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.MatchDto;
import com.pa.asvblrapi.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {})
public interface MatchMapper extends EntityMapper<MatchDto, Match> {
    MatchMapper instance = Mappers.getMapper(MatchMapper.class);

    @Mapping(source = "team.id", target = "idTeam")
    MatchDto toDto(Match match);
}
