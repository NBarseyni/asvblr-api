package com.pa.asvblrapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
}
