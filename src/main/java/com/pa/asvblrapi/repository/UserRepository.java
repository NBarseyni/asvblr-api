package com.pa.asvblrapi.repository;

import com.pa.asvblrapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query(value = "select u.* from user u, user_roles ur, role r where u.id = ur.user_id " +
            "and ur.role_id = r.id and r.name = \"ROLE_PRESIDENT\"", nativeQuery = true)
    User findPresident();
}
