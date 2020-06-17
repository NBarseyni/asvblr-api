package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdatePasswordDto {
    private String oldPassword;
    private String password;
}
