package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ArticleListDto {
    private Long id;
    private String title;
    private Date creationDate;
    private Date lastModificationDate;
    private boolean visible;
}
