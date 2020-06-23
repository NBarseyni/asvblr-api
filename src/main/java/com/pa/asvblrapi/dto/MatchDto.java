package com.pa.asvblrapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchDto {
    private Long id;
    @NotNull
    private Date date;
    @NotBlank
    private String place;
    @NotNull
    private boolean type;
    @NotBlank
    private String oppositeTeam;
    private String comment;
    private int technicalRating;
    private int collectiveRating;
    private int offensiveRating;
    private int defensiveRating;
    private int combativenessRating;
    @NotNull
    private Long idTeam;
}
