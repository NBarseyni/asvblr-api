package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.Drive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriveRepository extends JpaRepository<Drive, Long> {
}
