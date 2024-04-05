package com.naz.libManager.repository;

import com.naz.libManager.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM users u WHERE u.role= com.naz.libManager.enums.Role.PATRON")
    Page<User> findAllPatrons(Pageable pageable);

    Optional<User> findByEmailAddress(String emailAddress);
}
