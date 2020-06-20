package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query(value = "select t.* from team t, jersey j where t.id = j.team_id and j.player_id = :idPlayer",
            nativeQuery = true)
    List<Team> findAllByPlayer(@Param("idPlayer") Long idPlayer);
}
