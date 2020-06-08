package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.UserDto;
import com.pa.asvblrapi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface UserMapper extends EntityMapper<UserDto, User> {
    UserMapper instance = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);
    List<UserDto> toDto(List<User> users);
}
