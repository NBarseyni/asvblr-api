package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.DriveDto;
import com.pa.asvblrapi.entity.Drive;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface DriveMapper extends EntityMapper<DriveDto, Drive> {
    DriveMapper instance = Mappers.getMapper(DriveMapper.class);

    @Mapping(source = "driver.id", target = "idDriver")
    @Mapping(source = "driver.firstName", target = "firstNameDriver")
    @Mapping(source = "driver.lastName", target = "lastNameDriver")
    @Mapping(source = "match.id", target = "idMatch")
    DriveDto toDto(Drive drive);
    List<DriveDto> toDto(List<Drive> drives);
}
