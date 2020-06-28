package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Document findByName(String name);
}
