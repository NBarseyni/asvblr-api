package com.pa.asvblrapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private boolean gender;
    @NotNull
    private String address;
    @NotNull
    private int postcode;
    @NotNull
    private String city;
    @NotNull
    private String email;
    private String phoneNumber;
    @NotNull
    private Date birthDate;
    @NotNull
    private String nationality;
    @ManyToOne
    @JoinColumn(name = "topSize_id")
    private ClothingSize topSize;
    @ManyToOne
    @JoinColumn(name = "pantsSize_id")
    private ClothingSize pantsSize;
    private int requestedJerseyNumber;
    @NotNull
    private boolean insuranceRequested;
    @NotNull
    private boolean equipment;
    @NotNull
    private boolean referee;
    @NotNull
    private boolean coach;
    @NotNull
    private boolean validated;
    @NotNull
    private boolean calendarRequested;
    private String comment;
    private boolean pc_allowToLeaveAlone;
    private boolean pc_allowClubToRescue;
    private boolean pc_allowToTravelWithTeamMate;
    private boolean pc_allowToPublish;
    private boolean pc_unaccountability;
    private boolean pc_allowToWhatsapp;
    private Date creationDate;
    private Date validationDate;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    @JsonBackReference
    private Season season;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private SubscriptionCategory subscriptionCategory;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "subscription_payment_modes",
            joinColumns = @JoinColumn(name = "subscription_id"),
            inverseJoinColumns = @JoinColumn(name = "paymentMode_id"))
    private List<PaymentMode> paymentModes;

    @ManyToOne
    @JoinColumn(name = "CNI_id")
    private Document CNI;

    @ManyToOne
    @JoinColumn(name = "identityPhoto_id")
    private Document identityPhoto;

    @ManyToOne
    @JoinColumn(name = "medicalCertificate_id")
    private Document medicalCertificate;

    public Subscription(String firstName, String lastName, boolean gender, String address, int postcode, String city,
                        String email, String phoneNumber, Date birthDate, String nationality, ClothingSize topSize,
                        ClothingSize pantsSize, int requestedJerseyNumber, boolean insuranceRequested, boolean equipment,
                        boolean referee, boolean coach, boolean calendarRequested, String comment, boolean pc_allowToLeaveAlone,
                        boolean pc_allowClubToRescue, boolean pc_allowToTravelWithTeamMate, boolean pc_allowToPublish,
                        boolean pc_unaccountability, boolean pc_allowToWhatsapp, Season season,
                        SubscriptionCategory subscriptionCategory, List<PaymentMode> paymentModes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.topSize = topSize;
        this.pantsSize = pantsSize;
        this.equipment = equipment;
        this.referee = referee;
        this.coach = coach;
        this.calendarRequested = calendarRequested;
        this.validated = false;
        this.requestedJerseyNumber = requestedJerseyNumber;
        this.insuranceRequested = insuranceRequested;
        this.comment = comment;
        this.pc_allowToLeaveAlone = pc_allowToLeaveAlone;
        this.pc_allowClubToRescue = pc_allowClubToRescue;
        this.pc_allowToTravelWithTeamMate = pc_allowToTravelWithTeamMate;
        this.pc_allowToPublish = pc_allowToPublish;
        this.pc_unaccountability = pc_unaccountability;
        this.pc_allowToWhatsapp = pc_allowToWhatsapp;
        this.creationDate = new Date();
        this.season = season;
        this.subscriptionCategory = subscriptionCategory;
        this.paymentModes = paymentModes;
    }
}

























