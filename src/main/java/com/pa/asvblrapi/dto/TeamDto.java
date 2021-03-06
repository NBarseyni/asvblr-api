package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TeamDto {
    private Long id;
    @NotBlank
    private String name;
    private String photo;
    private Long idSeason;
    private Long idCoach;
    private Long idLeader;
    @NotNull
    private Long idTeamCategory;
}
