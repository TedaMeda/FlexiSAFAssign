package com.ticketer.ticketer.repository;

import com.ticketer.ticketer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
