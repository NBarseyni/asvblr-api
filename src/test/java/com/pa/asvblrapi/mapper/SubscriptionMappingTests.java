package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.SubscriptionDto;
import com.pa.asvblrapi.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class SubscriptionMappingTests {
    @Test
    public void should_map_subscription_to_dto() {
        ClothingSize clothingSize = new ClothingSize("L");
        Player player = new Player();
        player.setId((long)2);
        Season season = new Season();
        season.setId((long)3);
        Category category = new Category();
        category.setId((long)4);
        List<PaymentMode> paymentModes = new ArrayList<>();
        PaymentMode paymentMode1 = new PaymentMode();
        paymentMode1.setId((long)5);
        PaymentMode paymentMode2 = new PaymentMode();
        paymentMode2.setId((long)6);
        paymentModes.add(paymentMode1);
        paymentModes.add(paymentMode2);
        Document cni = new Document();
        cni.setId((long)7);
        Document identityPhoto = new Document();
        identityPhoto.setId((long)8);
        Document medicalCertificate = new Document();
        medicalCertificate.setId((long)9);

        Subscription subscription = new Subscription((long)1, "firstName", "lastName",
                true, "address", 75001, "city", "email",
                "phoneNumber", new Date(), "France", clothingSize, clothingSize,
                0, true, true, true, true, false,
                true, "comment", true, true,
                true, false, false, false,
                new Date(), new Date(), player, season, category, paymentModes, cni, identityPhoto, medicalCertificate);

        SubscriptionDto subscriptionDto = SubscriptionMapper.instance.toDto(subscription);

        assertThat(subscriptionDto).isNotNull();
        assertThat(subscriptionDto.getId()).isEqualTo(subscription.getId());
        assertThat(subscriptionDto.getFirstName()).isEqualTo(subscription.getFirstName());
        assertThat(subscriptionDto.getLastName()).isEqualTo(subscription.getLastName());
        assertThat(subscriptionDto.isGender()).isEqualTo(subscription.isGender());
        assertThat(subscriptionDto.getAddress()).isEqualTo(subscription.getAddress());
        assertThat(subscriptionDto.getPostcode()).isEqualTo(subscription.getPostcode());
        assertThat(subscriptionDto.getCity()).isEqualTo(subscription.getCity());
        assertThat(subscriptionDto.getEmail()).isEqualTo(subscription.getEmail());
        assertThat(subscriptionDto.getPhoneNumber()).isEqualTo(subscription.getPhoneNumber());
        assertThat(subscriptionDto.getBirthDate()).isEqualTo(subscription.getBirthDate());
        assertThat(subscriptionDto.getNationality()).isEqualTo(subscription.getNationality());
        assertThat(subscriptionDto.getIdTopSize()).isEqualTo(clothingSize.getId());
        assertThat(subscriptionDto.getIdPantsSize()).isEqualTo(clothingSize.getId());
        assertThat(subscriptionDto.getRequestedJerseyNumber()).isEqualTo(subscription.getRequestedJerseyNumber());
        assertThat(subscriptionDto.isInsuranceRequested()).isEqualTo(subscription.isInsuranceRequested());
        assertThat(subscriptionDto.isEquipment()).isEqualTo(subscription.isEquipment());
        assertThat(subscriptionDto.isReferee()).isEqualTo(subscription.isReferee());
        assertThat(subscriptionDto.isCoach()).isEqualTo(subscription.isCoach());
        assertThat(subscriptionDto.isValidated()).isEqualTo(subscription.isValidated());
        assertThat(subscriptionDto.isCalendar()).isEqualTo(subscription.isCalendar());
        assertThat(subscriptionDto.getComment()).isEqualTo(subscription.getComment());
        assertThat(subscriptionDto.isPc_allowToLeaveAlone()).isEqualTo(subscription.isPc_allowToLeaveAlone());
        assertThat(subscriptionDto.isPc_allowClubToRescue()).isEqualTo(subscription.isPc_allowClubToRescue());
        assertThat(subscriptionDto.isPc_allowToTravelWithTeamMate()).isEqualTo(subscription.isPc_allowToTravelWithTeamMate());
        assertThat(subscriptionDto.isPc_allowToPublish()).isEqualTo(subscription.isPc_allowToPublish());
        assertThat(subscriptionDto.isPc_unaccountability()).isEqualTo(subscription.isPc_unaccountability());
        assertThat(subscriptionDto.isPc_allowToWhatsapp()).isEqualTo(subscription.isPc_allowToWhatsapp());
        assertThat(subscriptionDto.getCreationDate()).isEqualTo(subscription.getCreationDate());
        assertThat(subscriptionDto.getValidationDate()).isEqualTo(subscription.getValidationDate());
        assertThat(subscriptionDto.getIdPlayer()).isEqualTo(player.getId());
        assertThat(subscriptionDto.getIdSeason()).isEqualTo(season.getId());
        assertThat(subscriptionDto.getIdCategory()).isEqualTo(category.getId());
        assertThat(subscriptionDto.getIdsPaymentMode().size()).isEqualTo(subscriptionDto.getIdsPaymentMode().size());
        assertThat(subscriptionDto.getIdCNI()).isEqualTo(cni.getId());
        assertThat(subscriptionDto.getIdIdentityPhoto()).isEqualTo(identityPhoto.getId());
        assertThat(subscriptionDto.getIdMedicalCertificate()).isEqualTo(medicalCertificate.getId());
    }
}
