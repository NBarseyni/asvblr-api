package com.pa.asvblrapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
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
    private String licenceNumber;
    @NotNull
    private String jerseySize;
    @NotNull
    private String pantSize;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "jersey_id")
    private Jersey jersey;

    @OneToMany(mappedBy = "player")
    private List<Subscription> subscriptions;

    public Player(String firstName, String lastName, String address, int postcode, String city, String email,
                  String phoneNumber, Date birthDate, String jerseySize, String pantSize, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.jerseySize = jerseySize;
        this.pantSize = pantSize;
        this.user = user;
    }
}
