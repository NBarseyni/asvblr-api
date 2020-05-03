package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PlayerDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String email;
    private int phoneNumber;
    @NotBlank
    private String licenceNumber;
    @NotBlank
    private String jerseySize;
    @NotBlank
    private String pantSize;
    @NotNull
    private Long idUser;
    @NotNull
    private Long idJerseyNumber;
}
