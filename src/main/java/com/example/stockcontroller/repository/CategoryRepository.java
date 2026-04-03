package com.example.stockcontroller.repository;

import com.example.stockcontroller.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> id(Long id);

    Page<Category> findByNameContainingIgnoreCase(String search, Pageable pageable);
}
