package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.SeasonDto;
import com.pa.asvblrapi.entity.Season;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface SeasonMapper extends EntityMapper<SeasonDto, Season> {
    SeasonMapper instance = Mappers.getMapper(SeasonMapper.class);

    SeasonDto toDto(Season season);
    List<SeasonDto> toDto(List<Season> seasons);
}
