package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.SubscriptionDto;
import com.pa.asvblrapi.entity.PaymentMode;
import com.pa.asvblrapi.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface SubscriptionMapper extends EntityMapper<SubscriptionDto, Subscription> {
    SubscriptionMapper instance = Mappers.getMapper(SubscriptionMapper.class);

    @Mapping(source = "player.id", target = "idPlayer")
    @Mapping(source = "season.id", target = "idSeason")
    @Mapping(source = "subscriptionCategory.id", target = "idSubscriptionCategory")
    @Mapping(source = "paymentModes", target = "idsPaymentMode")
    @Mapping(source = "CNI.id", target = "idCNI")
    @Mapping(source = "identityPhoto.id", target = "idIdentityPhoto")
    @Mapping(source = "medicalCertificate.id", target = "idMedicalCertificate")
    @Mapping(source = "topSize.id", target = "idTopSize")
    @Mapping(source = "pantsSize.id", target = "idPantsSize")
    SubscriptionDto toDto(Subscription subscription);

    List<SubscriptionDto> toDto(List<Subscription> subscriptions);

    default List<Long> listPaymentModeToListIdPaymentMode(List<PaymentMode> paymentModes) {
        List<Long> ids = new ArrayList<>();
        for (PaymentMode paymentMode : paymentModes) {
            ids.add(paymentMode.getId());
        }
        return ids;
    }
}
