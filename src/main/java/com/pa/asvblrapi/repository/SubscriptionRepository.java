package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query(value = "select s.* from Subscription s where s.season_id = :id",
            nativeQuery = true
    )
    List<Subscription> findSubscriptionBySeason(@Param("id") Long id);
}
