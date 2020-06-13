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
public class Drive {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String address;
    @NotNull
    private boolean go;
    @NotNull
    private int nbTotalPlaces;
    @NotNull
    private int nbFreePlaces;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User driver;
    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "drives_users",
            joinColumns = @JoinColumn(name = "drive_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> passengers;

    public Drive(String address, boolean go, int nbTotalPlaces, User driver, Match match) {
        this.address = address;
        this.go = go;
        this.nbTotalPlaces = nbTotalPlaces;
        this.nbFreePlaces = nbTotalPlaces;
        this.driver = driver;
        this.match = match;
    }
}
