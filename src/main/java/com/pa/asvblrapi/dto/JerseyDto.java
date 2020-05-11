package com.pa.asvblrapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JerseyDto {
    private Long id;
    @NotBlank
    private String number;
    @NotNull
    private Long idTeam;
    @NotNull
    private Long idPosition;
}