package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query(value = "select m.* from Matche m where m.team_id = :idTeam",
            nativeQuery = true)
    List<Match> findAllByIdTeam(@Param("idTeam") Long idTeam);
}
