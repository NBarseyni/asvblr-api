package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(value = "select a.* from Article a order by a.last_modification_date DESC",
            countQuery = "select count(a.id) from Article a",
            nativeQuery = true
    )
    Page<Article> findAllPage(Pageable pageable);
}
