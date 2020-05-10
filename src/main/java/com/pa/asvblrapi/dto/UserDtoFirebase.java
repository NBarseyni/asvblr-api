package com.pa.asvblrapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoFirebase {
    private String username;
    private String lastName;
    private String firstName;
    private String email;
}
