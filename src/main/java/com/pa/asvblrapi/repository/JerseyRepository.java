package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.Jersey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JerseyRepository extends JpaRepository<Jersey, Long> {
}
