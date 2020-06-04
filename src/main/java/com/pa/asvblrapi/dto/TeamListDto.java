package com.pa.asvblrapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeamListDto {
    private Long id;
    private String teamCategoryName;
    private String coachName;
    private int nbPlayers;
}
