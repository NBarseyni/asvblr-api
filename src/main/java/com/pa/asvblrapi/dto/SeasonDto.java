package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SeasonDto {
    private Long id;
    @NotBlank
    private String name;
}
