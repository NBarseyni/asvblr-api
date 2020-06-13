package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DriveUserDto {
    @NotNull
    private Long idUser;
}
