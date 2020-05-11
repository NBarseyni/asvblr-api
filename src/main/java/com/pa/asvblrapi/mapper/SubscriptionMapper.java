package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.SubscriptionDto;
import com.pa.asvblrapi.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {})
public interface SubscriptionMapper extends EntityMapper<SubscriptionDto, Subscription> {
    SubscriptionMapper instance = Mappers.getMapper(SubscriptionMapper.class);

    @Mapping(source = "player.id", target = "idPlayer")
    @Mapping(source = "season.id", target = "idSeason")
    @Mapping(source = "category.id", target = "idCategory")
    @Mapping(source = "paymentMode.id", target = "idPaymentMode")
    @Mapping(source = "CNI.id", target = "idCNI")
    @Mapping(source = "identityPhoto.id", target = "idIdentityPhoto")
    @Mapping(source = "medicalCertificate.id", target = "idMedicalCertificate")
    @Mapping(source = "topSize.id", target = "idTopSize")
    @Mapping(source = "pantsSize.id", target = "idPantsSize")
    SubscriptionDto toDto(Subscription subscription);
}
