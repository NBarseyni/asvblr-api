package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.Player;
import com.pa.asvblrapi.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    @Query(value = "select p.* from Player p where p.user_id = :idUser",
            nativeQuery = true
    )
    Optional<Player> findByIdUser(@Param("idUser") Long idUser);
}
