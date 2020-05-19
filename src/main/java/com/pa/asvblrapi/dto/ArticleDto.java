package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
public class ArticleDto {
    private Long id;
    @NotBlank
    private String title;
    private String content;
    private Date creationDate;
    private Date lastModificationDate;
    private boolean visible;
}
