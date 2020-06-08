package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AddPlayerTeamDto {
    @NotNull
    private Long idPlayer;
    private Long idPosition;
    private int number;
}
