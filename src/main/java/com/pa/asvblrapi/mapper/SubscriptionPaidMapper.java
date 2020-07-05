package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.SubscriptionPaidDto;
import com.pa.asvblrapi.entity.SubscriptionPaid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface SubscriptionPaidMapper extends EntityMapper<SubscriptionPaidDto, SubscriptionPaid> {
    SubscriptionPaidMapper instance = Mappers.getMapper(SubscriptionPaidMapper.class);

    @Mapping(source = "paymentMode.id", target = "idPaymentMode")
    @Mapping(source = "paymentMode.name", target = "namePaymentMode")
    SubscriptionPaidDto toDto(SubscriptionPaid subscriptionPaid);

    List<SubscriptionPaidDto> toDto(List<SubscriptionPaid> subscriptionsPaid);
}
