package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DriveDto {
    private Long id;
    @NotBlank
    private String address;
    @NotNull
    private boolean go;
    @NotNull
    private int nbTotalPlaces;
    private int nbFreePlaces;
    @NotNull
    private Long idDriver;
    @NotNull
    private Long idMatch;
}
