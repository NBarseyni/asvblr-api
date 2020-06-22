package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSavePasswordDto {
    private String token;
    private String password;
}
