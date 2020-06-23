package com.pa.asvblrapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean passwordChanged;
    private Long idPlayer;
    private List<String> roles;
    private List<String> privileges;
}
