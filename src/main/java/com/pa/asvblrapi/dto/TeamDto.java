package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TeamDto {
    @NotBlank
    private String name;
    private String photo;
    @NotNull
    private Long idSeason;
}
