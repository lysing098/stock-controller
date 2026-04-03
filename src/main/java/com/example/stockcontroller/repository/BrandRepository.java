package com.example.stockcontroller.repository;

import com.example.stockcontroller.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Page<Brand> findByNameContainingIgnoreCase(String search, Pageable pageable);
}
