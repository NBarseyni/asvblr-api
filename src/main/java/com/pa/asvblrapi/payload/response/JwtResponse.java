package com.pa.asvblrapi.payload.response;

import com.pa.asvblrapi.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private UserDto user;

    public JwtResponse(String accessToken, Long id, String username, String firstName, String lastName,
                       String email, boolean isPasswordChanged, Long idPlayer, List<String> roles, List<String> privileges) {
        this.token = accessToken;
        this.user = new UserDto(id, username, firstName, lastName, email, isPasswordChanged, idPlayer, roles, privileges);
    }
}
