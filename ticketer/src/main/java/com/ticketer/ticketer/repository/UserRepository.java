package com.ticketer.ticketer.repository;

import com.ticketer.ticketer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByActive(boolean active);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
