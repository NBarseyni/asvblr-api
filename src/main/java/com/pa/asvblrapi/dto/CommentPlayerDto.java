package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CommentPlayerDto {
    private Long id;
    @NotBlank
    private String comment;
    @NotNull
    private int rate;
    @NotNull
    private Long idJersey;
    @NotNull
    private Long idMatch;
}
