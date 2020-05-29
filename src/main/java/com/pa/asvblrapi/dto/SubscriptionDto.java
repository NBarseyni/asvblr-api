package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

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
    @NotNull
    private boolean calendar;
    // Parental authorisation
    private boolean pc_allowToLeaveAlone;
    private boolean pc_allowClubToRescue;
    private boolean pc_allowToTravelWithTeamMate;
    private boolean pc_allowToPublish;
    private boolean pc_unaccountability;
    private boolean pc_allowToWhatsapp;
    private Date creationDate;
    private Date validationDate;
    private Long idPlayer;
    private Long idSeason;
    @NotNull
    private Long idCategory;
    @NotNull
    private List<Long> idsPaymentMode;
    private Long idCNI;
    private Long idIdentityPhoto;
    private Long idMedicalCertificate;
}
