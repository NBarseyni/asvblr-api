package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class SubscriptionDto {
    private Long id;
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
    private String nationality;
    private Long idTopSize;
    private Long idPantsSize;
    private String comment;
    private int requestedJerseyNumber;
    @NotNull
    private boolean insuranceRequested;
    @NotNull
    private boolean equipment;
    @NotNull
    private boolean referee;
    @NotNull
    private boolean coach;
    private boolean validated;
    private Date creationDate;
    private Date validationDate;
    private Long idPlayer;
    private Long idSeason;
    @NotNull
    private Long idCategory;
    @NotNull
    private Long idPaymentMode;
    private Long idCNI;
    private Long idIdentityPhoto;
    private Long idMedicalCertificate;
}
