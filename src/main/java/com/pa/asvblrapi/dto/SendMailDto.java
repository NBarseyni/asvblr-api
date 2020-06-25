package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class SendMailDto {
    @NotNull
    private List<Long> idsUser;
    @NotBlank
    private String object;
    @NotBlank
    private String content;
}
