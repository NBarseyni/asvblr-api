package com.pa.asvblrapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String name;

    private String photo;

    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private User coach;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "leader_id", referencedColumnName = "id")
    private Jersey leader;

    @ManyToOne
    @JoinColumn(name = "team_category_id", nullable = false)
    private TeamCategory teamCategory;

    @OneToMany(mappedBy = "team")
    private List<Jersey> jerseys;

    @OneToMany(mappedBy = "team")
    private List<Match> matches;

    public Team(String name, Season season, TeamCategory teamCategory) {
        this.name = name;
        this.season = season;
        this.teamCategory = teamCategory;
    }
}
