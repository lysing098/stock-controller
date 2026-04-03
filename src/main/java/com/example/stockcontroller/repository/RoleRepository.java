package com.example.stockcontroller.repository;

import com.example.stockcontroller.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Page<Role> findByNameContainingIgnoreCase(String search, Pageable pageable);
}
