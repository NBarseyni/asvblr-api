package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.TeamCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamCategoryRepository extends JpaRepository<TeamCategory, Long> {

    TeamCategory findByName(String name);
}
