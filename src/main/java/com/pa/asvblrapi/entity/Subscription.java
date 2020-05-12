package com.pa.asvblrapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

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
    @JoinColumn(name = "topSize_id", nullable = false)
    private ClothingSize topSize;
    @ManyToOne
    @JoinColumn(name = "pantsSize_id", nullable = false)
    private ClothingSize pantsSize;
    @NotNull
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
    private boolean confirmed;
    private String comment;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    @JsonBackReference
    private Season season;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "paymentMode_id", nullable = false)
    private PaymentMode paymentMode;

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
                        ClothingSize pantsSize, boolean insuranceRequested, boolean equipment, boolean referee, boolean coach,
                        Season season, Category category, PaymentMode paymentMode) {
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
        this.confirmed = false;
        this.insuranceRequested = insuranceRequested;
        this.season = season;
        this.category = category;
        this.paymentMode = paymentMode;
    }
}

























