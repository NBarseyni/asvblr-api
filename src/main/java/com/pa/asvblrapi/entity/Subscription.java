package com.pa.asvblrapi.entity;

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
    private int phoneNumber;
    @NotNull
    private Date birthDate;
    @NotNull
    private String birthCountry;
    @NotNull
    private boolean insurance;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false)
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
}
