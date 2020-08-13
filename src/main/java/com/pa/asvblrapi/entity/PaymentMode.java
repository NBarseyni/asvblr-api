package com.pa.asvblrapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PaymentMode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;

    @OneToMany(mappedBy = "paymentMode")
    @JsonBackReference
    private Set<SubscriptionPaid> subscriptionsPaid;

    public PaymentMode(String name) {
        super();
        this.name = name;
    }
}
