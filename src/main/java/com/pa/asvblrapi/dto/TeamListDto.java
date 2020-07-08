package com.pa.asvblrapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeamListDto {
    private Long id;
    private String teamName;
    private String teamCategoryName;
    private String coachFirstName;
    private String coachLastName;
    private Long idPlayerLeader;
    private String leaderFirstName;
    private String leaderLastName;
    private int nbPlayers;
}
