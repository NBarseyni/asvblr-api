package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.Drive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriveRepository extends JpaRepository<Drive, Long> {

    @Query(value = "select d.* from Drive d where d.match_id = :idMatch",
            nativeQuery = true)
    List<Drive> findAllByIdMatch(@Param("idMatch") Long idMatch);

    @Query(value = "select d.* from Drive d where d.driver_id = :idDriver",
            nativeQuery = true)
    List<Drive> findAllByIdDriver(@Param("idDriver") Long idDriver);

    @Query(value = "select d.* from Drive d, drives_users du where d.id = du.drive_id and du.user_id = :idPassenger",
            nativeQuery = true)
    List<Drive> findAllByIdPassenger(@Param("idPassenger") Long idPassenger);
}
