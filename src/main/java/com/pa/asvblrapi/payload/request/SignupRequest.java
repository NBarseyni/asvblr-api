package com.pa.asvblrapi.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 30)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 30)
    private String lastName;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
}
