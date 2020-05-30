package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AddPlayerTeamDto {
    @NotNull
    private Long idPlayer;
    @NotNull
    private Long idPosition;
    @NotNull
    private int number;
}
