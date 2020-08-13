package com.pa.asvblrapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CommentPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String comment;
    @NotNull
    private int rate;
    @ManyToOne
    @JoinColumn(name = "jersey_id")
    private Jersey jersey;
    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;
}
