package com.pa.asvblrapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @OneToMany(mappedBy = "team")
    private List<Photo> photos;

    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false)
    @JsonBackReference
    private Season season;

    @OneToMany(mappedBy = "team")
    private List<JerseyNumber> jerseyNumbers;

    public Team(String name, Season season) {
        this.name = name;
        this.season = season;
    }
}
