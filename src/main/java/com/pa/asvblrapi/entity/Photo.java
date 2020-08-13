package com.pa.asvblrapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = true)
    @JsonBackReference
    private Team team;

    public Photo(String name, Team team) {
        this.name = name;
        this.team = team;
    }

    public Photo(String name) {
        this.name = name;
    }
}
