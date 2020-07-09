package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query(value = "select s.* from subscription s where s.season_id = :id",
            nativeQuery = true
    )
    List<Subscription> findSubscriptionBySeason(@Param("id") Long id);

    @Query(value = "select s.* from subscription s where s.player_id = :id",
            nativeQuery = true
    )
    List<Subscription> findSubscriptionsByPlayer(@Param("id") Long id);

    @Query(value = "select p.id, p.name, count(p.id) from subscription s, subscription_paid sp, payment_mode p " +
            "where s.id = sp.subscription_id and sp.payment_mode_id = p.id and s.validated = 1 group by p.id",
            nativeQuery = true)
    List<Object> countNbPaymentMode();
}
