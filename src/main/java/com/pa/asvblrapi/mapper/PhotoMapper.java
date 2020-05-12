package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.PhotoDto;
import com.pa.asvblrapi.entity.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface PhotoMapper extends EntityMapper<PhotoDto, Photo> {
    PhotoMapper instance = Mappers.getMapper(PhotoMapper.class);

    @Mapping(source = "team.id", target = "idTeam")
    PhotoDto toDto(Photo photo);
    List<PhotoDto> toDto(List<Photo> photos);
}
