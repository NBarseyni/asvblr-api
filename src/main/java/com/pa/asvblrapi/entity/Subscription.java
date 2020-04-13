package com.pa.asvblrapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private String birthCountry;
    @NotNull
    private boolean insurance;

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

    @ManyToOne
    @JoinColumn(name = "formLicence_id")
    private Document formLicence;

    public Subscription(String firstName, String lastName, boolean gender, String address, int postcode, String city,
                        String email, String phoneNumber, Date birthDate, String birthCountry, boolean insurance,
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
        this.birthCountry = birthCountry;
        this.insurance = insurance;
        this.season = season;
        this.category = category;
        this.paymentMode = paymentMode;
    }
}

























