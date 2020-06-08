package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.UserDto;
import com.pa.asvblrapi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserMappingTests {

    @Test
    public void should_map_user_to_dto() {
        User user = new User("username", "firstname", "lastname", "email", "123456");
        user.setId((long)1);

        UserDto userDto = UserMapper.instance.toDto(user);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getUsername()).isEqualTo(user.getUsername());
        assertThat(userDto.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userDto.getLastName()).isEqualTo(user.getLastName());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
    }
}
