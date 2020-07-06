package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class DriveDto {
    private Long id;
    @NotBlank
    private String address;
    @NotNull
    private boolean go;
    @NotNull
    private Date date;
    @NotNull
    private int nbTotalPlaces;
    private int nbFreePlaces;
    @NotNull
    private Long idDriver;
    private String firstNameDriver;
    private String lastNameDriver;
    @NotNull
    private Long idMatch;
}
