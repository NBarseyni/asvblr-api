package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.UserDto;
import com.pa.asvblrapi.entity.Privilege;
import com.pa.asvblrapi.entity.Role;
import com.pa.asvblrapi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface UserMapper {
    UserMapper instance = Mappers.getMapper(UserMapper.class);

    default UserDto toDto(User user) {
        List<String> roles = new ArrayList<>();
        List<String> privileges = new ArrayList<>();
        if (user.getRoles() != null) {
            for (Role role :
                    user.getRoles()) {
                roles.add(role.getName());
                for (Privilege privilege :
                        role.getPrivileges()) {
                    if (!privileges.contains(privilege.getName())) {
                        privileges.add(privilege.getName());
                    }
                }
            }
        }

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.isPasswordChanged(),
                roles,
                privileges);
    }

    List<UserDto> toDto(List<User> users);
}
