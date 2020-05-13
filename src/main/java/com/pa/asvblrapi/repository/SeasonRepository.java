package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    @Query(value = "select s.* from Season s where s.current_season = 1",
            nativeQuery = true
    )
    Optional<Season> findCurrentSeason();
}
