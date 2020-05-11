package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.JerseyDto;
import com.pa.asvblrapi.entity.Jersey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {})
public interface JerseyMapper extends EntityMapper<JerseyDto, Jersey> {
    JerseyMapper instance = Mappers.getMapper(JerseyMapper.class);

    @Mapping(source = "team.id", target = "idTeam")
    @Mapping(source = "position.id", target = "idPosition")
    JerseyDto toDto(Jersey jersey);
}
