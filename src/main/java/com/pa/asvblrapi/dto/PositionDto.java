package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PositionDto {
    @NotBlank
    private String name;
    @NotBlank
    private String shortName;
}
