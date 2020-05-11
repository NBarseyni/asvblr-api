package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class PlayerDto {
    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String address;
    @NotNull
    private int postcode;
    @NotBlank
    private String city;
    @NotBlank
    private String email;
    private String phoneNumber;
    @NotNull
    private Date birthDate;
    @NotBlank
    private String licenceNumber;
    @NotNull
    private Long idTopSize;
    @NotNull
    private Long idPantsSize;
    @NotNull
    private Long idUser;
    @NotNull
    private Long idJersey;
}
