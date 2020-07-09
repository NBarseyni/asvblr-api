package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    Price findByName(String name);

    Optional<Price> findByCode(String code);
}
