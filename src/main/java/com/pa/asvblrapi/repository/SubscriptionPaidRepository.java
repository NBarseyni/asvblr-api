package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.SubscriptionPaid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPaidRepository extends JpaRepository<SubscriptionPaid, Long> {
}
