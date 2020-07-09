package com.pa.asvblrapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "matche")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private Date date;
    @NotNull
    private String place;
    @NotNull
    private boolean type;
    @NotNull
    private String oppositeTeam;
    private String comment;
    private int technicalRating;
    private int collectiveRating;
    private int offensiveRating;
    private int defensiveRating;
    private int combativenessRating;
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "match")
    private List<CommentPlayer> commentsPlayer;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "match")
    private List<Drive> drives;

    public Match(Date date, String place, boolean type, String oppositeTeam, Team team) {
        this.date = date;
        this.place = place;
        this.type = type;
        this.oppositeTeam = oppositeTeam;
        this.team = team;
    }
}
