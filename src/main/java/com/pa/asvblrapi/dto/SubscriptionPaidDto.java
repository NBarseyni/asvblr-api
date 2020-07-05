package com.pa.asvblrapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPaidDto {
    private Long idPaymentMode;
    private String namePaymentMode;
    private boolean paid;
}
