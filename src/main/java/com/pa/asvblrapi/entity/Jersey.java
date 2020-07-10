package com.pa.asvblrapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Jersey {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer number;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "jersey")
    private List<CommentPlayer> commentsPlayer;

    public Jersey(Integer number, Team team, Position position, Player player) {
        this.number = (number == 0) ? null : number;
        this.team = team;
        this.position = position;
        this.player = player;
    }
}
