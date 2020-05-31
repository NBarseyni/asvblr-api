package com.pa.asvblrapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CommentPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String comment;
    private int rate;
    @ManyToOne
    @JoinColumn(name = "jersey_id")
    private Jersey jersey;
    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;
}
