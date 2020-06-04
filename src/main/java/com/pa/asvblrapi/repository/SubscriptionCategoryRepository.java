package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.SubscriptionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionCategoryRepository extends JpaRepository<SubscriptionCategory, Long> {

    SubscriptionCategory findByName(String name);
}
