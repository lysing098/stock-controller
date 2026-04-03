package com.example.stockcontroller.service;

import com.example.stockcontroller.exception.MyResourceNotFoundException;
import com.example.stockcontroller.model.Category;
import com.example.stockcontroller.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Page<Category> getAllCategories(String search , int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if(search == null || search.isEmpty()) {
            return categoryRepository.findAll(pageable);
        }
        return categoryRepository.findByNameContainingIgnoreCase(search,pageable);
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Category update(Long id, Category category) {
        Category existing = categoryRepository.findById(id).orElseThrow(() -> new MyResourceNotFoundException("Category not found with id " + id));
        existing.setName(category.getName());
        return categoryRepository.save(existing);

    }

    public Category delete(Long id) {
        Category existing  = categoryRepository.findById(id).orElseThrow(() -> new MyResourceNotFoundException("Category not found with id"+id));

        categoryRepository.delete(existing);
        return existing;


    }
}
