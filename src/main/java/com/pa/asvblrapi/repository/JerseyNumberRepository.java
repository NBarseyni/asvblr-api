package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.JerseyNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JerseyNumberRepository extends JpaRepository<JerseyNumber, Long> {
}
