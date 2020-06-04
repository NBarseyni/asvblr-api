package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.TeamDto;
import com.pa.asvblrapi.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface TeamMapper extends EntityMapper<TeamDto, Team> {
    TeamMapper instance = Mappers.getMapper(TeamMapper.class);

    @Mapping(source = "season.id", target = "idSeason")
    @Mapping(source = "coach.id", target = "idCoach")
    @Mapping(source = "teamCategory.id", target = "idTeamCategory")
    @Mapping(source = "leader.player.id", target = "idLeader")
    TeamDto toDto(Team team);

    List<TeamDto> toDto(List<Team> teams);
}
