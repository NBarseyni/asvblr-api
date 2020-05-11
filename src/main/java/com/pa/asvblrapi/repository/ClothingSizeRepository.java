package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.ClothingSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothingSizeRepository extends JpaRepository<ClothingSize, Long> {

    ClothingSize findByName(String name);
}
