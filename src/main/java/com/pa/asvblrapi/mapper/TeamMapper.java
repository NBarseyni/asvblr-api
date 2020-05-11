package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.TeamDto;
import com.pa.asvblrapi.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {})
public interface TeamMapper extends EntityMapper<TeamDto, Team> {
    TeamMapper instance = Mappers.getMapper(TeamMapper.class);

    @Mapping(source = "season.id", target = "idSeason")
    TeamDto toDto(Team team);
}
