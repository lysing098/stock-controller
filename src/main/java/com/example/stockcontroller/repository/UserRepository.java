package com.example.stockcontroller.repository;

import com.example.stockcontroller.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByUserNameContainingIgnoreCase(String search, Pageable pageable);

//    Optional<User> findByUserName(String username);

    Optional<User> findByEmail(String email);
}
