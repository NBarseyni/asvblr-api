package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.RoleDto;
import com.pa.asvblrapi.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface RoleMapper extends EntityMapper<RoleDto, Role> {
    RoleMapper instance = Mappers.getMapper(RoleMapper.class);

    RoleDto toDto(Role role);
    List<RoleDto> toDto(List<Role> roles);
}
