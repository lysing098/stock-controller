package com.example.stockcontroller.repository;

import com.example.stockcontroller.model.Supply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplyRepository extends JpaRepository<Supply, Long> {
    Page<Supply> findByProduct_NameContainingIgnoreCase(String productName, Pageable pageable);
}
