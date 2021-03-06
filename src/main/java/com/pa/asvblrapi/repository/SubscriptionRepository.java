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

    @Query(value = "select p.name, count(p.name) from subscription s, subscription_paid sp, payment_mode p, season se " +
            "where s.id = sp.subscription_id and sp.payment_mode_id = p.id and s.season_id = se.id and s.validated = 1 " +
            "and se.current_season = 1 group by p.name",
            nativeQuery = true)
    List<Object> countNbPaymentMode();
}
