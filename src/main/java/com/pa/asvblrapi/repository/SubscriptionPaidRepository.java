package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.SubscriptionPaid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionPaidRepository extends JpaRepository<SubscriptionPaid, Long> {

    @Query(value = "select * from subscription_paid s where s.subscription_id = :idSubscription " +
            "and s.payment_mode_id = :idPaymentMode",
            nativeQuery = true)
    Optional<SubscriptionPaid> findByIdSubscriptionAndIdPaymentMode(
            @Param("idSubscription") Long idSubscription, @Param("idPaymentMode") Long idPaymentMode);

    @Query(value = "select * from subscription_paid s where s.subscription_id = :id",
            nativeQuery = true)
    List<SubscriptionPaid> findByIdSubscription(@Param("id") Long id);
}
