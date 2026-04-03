package com.example.stockcontroller.repository;

import com.example.stockcontroller.model.Product;
import com.example.stockcontroller.model.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Page<Stock> findByProduct_NameContainingIgnoreCase(String search, Pageable pageable);

    Optional<Stock> findByProduct(Product product);
}