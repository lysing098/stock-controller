package com.example.stockcontroller.controller;

import com.example.stockcontroller.dto.PaginateResponse;
import com.example.stockcontroller.model.Category;
import com.example.stockcontroller.repository.CategoryRepository;
import com.example.stockcontroller.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllCategories(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size

    ) {
        Page<Category> pageResult = categoryService.getAllCategories(search,page,size);
        PaginateResponse<Category> response = new PaginateResponse<>(
                pageResult.getContent(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isFirst(),
                pageResult.isLast()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody Category category) {
        var category1 = categoryService.create(category);
        HashMap<String,Object> response = new HashMap<>();
        response.put("message","Category created successfully");
        response.put("category", category1);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@Valid @RequestBody Category category) {
        var category1 = categoryService.update(id,category);
        HashMap<String,Object> response = new HashMap<>();
        response.put("message","Category updated successfully");
        response.put("category", category1);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var category = categoryService.delete(id);
        HashMap<String,Object> response = new HashMap<>();
        response.put("message","Category deleted successfully");
        response.put("category", category);
        return ResponseEntity.ok(response);
    }
}
