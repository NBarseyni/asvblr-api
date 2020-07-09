package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query(value = "select v.page_code, MONTH(v.date) as date, count(*)" +
            "from visit v where date > DATE_SUB(NOW(), INTERVAL 12 MONTH)" +
            "group by v.page_code, date order by date DESC",
            nativeQuery = true)
    List<Object> getVisitStats();
}
