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
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String title;
    @Lob
    @Column(length = 100000)
    @NotNull
    private String content;
    @NotNull
    private Date creationDate;
    @NotNull
    private Date lastModificationDate;
    @NotNull
    private boolean visible;

    public Article(String title, String content) {
        this.title = title;
        this.content = content;
        this.creationDate = new Date();
        this.lastModificationDate = new Date();
        this.visible = false;
    }
}
