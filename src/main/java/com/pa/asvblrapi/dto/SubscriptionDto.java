package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class SubscriptionDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private boolean gender;
    @NotBlank
    private String address;
    @NotNull
    private int postcode;
    @NotBlank
    private String city;
    @NotBlank
    private String email;
    private String phoneNumber;
    @NotNull
    private Date birthDate;
    @NotBlank
    private String birthCountry;
    @NotBlank
    private String topSize;
    @NotBlank
    private String pantsSize;
    @NotNull
    private int requestedJerseyNumber;
    @NotNull
    private boolean insurance;
    @NotNull
    private boolean equipment;
    @NotNull
    private boolean referee;
    @NotNull
    private boolean coach;
    @NotNull
    private Long idSeason;
    @NotNull
    private Long idCategory;
    @NotNull
    private Long idPaymentMode;

}
