package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    @Query(value = "select p.* from player p where p.user_id = :idUser",
            nativeQuery = true
    )
    Optional<Player> findByIdUser(@Param("idUser") Long idUser);

    @Query(value = "select count(city), city, postcode from player group by city, postcode",
            nativeQuery = true)
    List<Object> countNbPlayersByCity();

    @Query(value = "SELECT SUM(IF(TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 0 and 4, 1, 0)) as '0 - 4'," +
            "SUM(IF(TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 5 and 9, 1, 0)) as '5 - 9'," +
            "SUM(IF(TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 10 and 14, 1, 0)) as '10 - 14'," +
            "SUM(IF(TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 15 and 19, 1, 0)) as '15 - 19'," +
            "SUM(IF(TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 20 and 24, 1, 0)) as '20 - 24'," +
            "SUM(IF(TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 25 and 29, 1, 0)) as '25 - 29'," +
            "SUM(IF(TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 30 and 34, 1, 0)) as '30 - 34'," +
            "SUM(IF(TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 35 and 39, 1, 0)) as '35 - 39'," +
            "SUM(IF(TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 40 and 44, 1, 0)) as '40 - 44'," +
            "SUM(IF(TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 45 and 49, 1, 0)) as '45 - 49'," +
            "SUM(IF(TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 50 and 54, 1, 0)) as '50 - 54'," +
            "SUM(IF(TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 55 and 59, 1, 0)) as '55 - 59'," +
            "SUM(IF(TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 60 and 64, 1, 0)) as '60 - 64'," +
            "SUM(IF(TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 65 and 69, 1, 0)) as '65 - 69'," +
            "SUM(IF(TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 70 and 74, 1, 0)) as '70 - 74'," +
            "SUM(IF(TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 75 and 79, 1, 0)) as '75 - 79'" +
            "FROM player",
            nativeQuery = true)
    List<Object> countNbPlayersByAgeByTrancheOf5();
}
