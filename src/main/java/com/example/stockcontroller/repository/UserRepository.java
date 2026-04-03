package com.example.stockcontroller.repository;

import com.example.stockcontroller.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByUserNameContainingIgnoreCase(String search, Pageable pageable);
}
