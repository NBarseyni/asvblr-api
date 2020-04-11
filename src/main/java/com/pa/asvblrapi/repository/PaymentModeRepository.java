package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.PaymentMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentModeRepository extends JpaRepository<PaymentMode, Long> {

    PaymentMode findByName(String name);
}
