package com.example.stockcontroller.repository;

import com.example.stockcontroller.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    Page<Supplier> findByNameContainingIgnoreCase(String search, Pageable pageable);
}
