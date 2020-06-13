package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.Drive;
import com.pa.asvblrapi.entity.Match;
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
}
