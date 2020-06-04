package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.Jersey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JerseyRepository extends JpaRepository<Jersey, Long> {
    @Query(value = "select j.* from jersey j where j.team_id = :idTeam and j.player_id = :idPlayer",
            nativeQuery = true
    )
    Optional<Jersey> findByIdTeamAndIdPlayer(@Param("idTeam") Long idTeam, @Param("idPlayer") Long idPlayer);

    @Query(value = "select j.* from jersey j where j.team_id = :idTeam",
            nativeQuery = true)
    List<Jersey> findByIdTeam(@Param("idTeam") Long idTeam);

    @Query(value = "select count(j.id) from jersey j where j.team_id = :idTeam",
            nativeQuery = true)
    int nbPlayerByIdTeam(@Param("idTeam") Long idTeam);
}
