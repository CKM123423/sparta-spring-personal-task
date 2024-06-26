package com.sparta.spartaspringpersonaltask.domain.user.repository;

import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String email);

    Optional<User> findByUsername(String username);
}
