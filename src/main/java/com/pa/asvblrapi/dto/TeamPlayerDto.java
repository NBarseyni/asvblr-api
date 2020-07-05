package com.pa.asvblrapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamPlayerDto {
    private Long idPlayer;
    private String firstName;
    private String lastName;
    private String address;
    private int postcode;
    private String city;
    private String email;
    private String phoneNumber;
    private Date birthDate;
    private String licenceNumber;
    private Integer requestedJerseyNumber;

    private Integer jerseyNumber;

    private String positionName;
    private String positionShortName;
}
