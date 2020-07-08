package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ContactMailDto {
    @NotBlank
    @Email
    private String sender;
    @NotBlank
    private String content;
}
