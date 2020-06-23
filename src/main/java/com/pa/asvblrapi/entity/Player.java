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
    private String licenceNumber;
    @ManyToOne
    @JoinColumn(name = "topSize_id")
    private ClothingSize topSize;
    @ManyToOne
    @JoinColumn(name = "pantsSize_id")
    private ClothingSize pantsSize;
    @ManyToOne
    @JoinColumn(name = "subscriptionCategory_id")
    private SubscriptionCategory subscriptionCategory;

    //@OneToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne(mappedBy = "player")
    private User user;

    @OneToMany(mappedBy = "player")
    private List<Jersey> jerseys;

    @OneToMany(mappedBy = "player")
    private List<Subscription> subscriptions;

    public Player(String firstName, String lastName, boolean gender, String address, int postcode, String city,
                  String email, String phoneNumber, Date birthDate, ClothingSize topSize, ClothingSize pantsSize,
                  SubscriptionCategory subscriptionCategory, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.topSize = topSize;
        this.pantsSize = pantsSize;
        this.subscriptionCategory = subscriptionCategory;
        this.user = user;
    }
}
