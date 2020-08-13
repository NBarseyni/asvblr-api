package com.pa.asvblrapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SubscriptionPaid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @ManyToOne
    @JoinColumn(name = "payment_mode_id")
    private PaymentMode paymentMode;

    @NotNull
    private boolean paid;

    public SubscriptionPaid(Subscription subscription, PaymentMode paymentMode) {
        this.subscription = subscription;
        this.paymentMode = paymentMode;
        this.paid = false;
    }
}
