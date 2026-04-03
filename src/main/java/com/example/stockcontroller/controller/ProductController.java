package com.example.stockcontroller.controller;

import com.example.stockcontroller.dto.PaginateResponse;
import com.example.stockcontroller.model.Brand;
import com.example.stockcontroller.model.Category;
import com.example.stockcontroller.model.Product;
import com.example.stockcontroller.model.User;
import com.example.stockcontroller.repository.BrandRepository;
import com.example.stockcontroller.repository.CategoryRepository;
import com.example.stockcontroller.repository.UserRepository;
import com.example.stockcontroller.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    public ResponseEntity<?> getAllProducts(
            @RequestParam(value = "search", required = false)  String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<Product> pageResult = productService.getAllProducts(search,size,page);

        List<Category> categories = categoryRepository.findAll();
        List<Brand> brands = brandRepository.findAll();
        List<User> users = userRepository.findAll();

        Map<String, Object> data = new HashMap<>();
        data.put("categories", categories);
        data.put("brands", brands);
        data.put("users", users);
        data.put("products", pageResult.getContent());

        List<Map<String, Object>> dataList = List.of(data);

        PaginateResponse<Map<String,Object>> response = new PaginateResponse<>(
            dataList,
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isFirst(),
                pageResult.isLast()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> create(
            @Valid @RequestParam("name") String name,
            @Valid @RequestParam("code") String code,
            @Valid @RequestParam("price") double price,
            @Valid @RequestParam("image") MultipartFile image,
            @Valid @RequestParam("category_id") Category category,
            @Valid @RequestParam("brand_id") Brand brand,
            @Valid @RequestParam("user_id") User user
    ) throws IOException {
        var product = productService.create(name,code,price,image,category,brand,user);
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Product created successfully");
        data.put("product", product);
        return ResponseEntity.ok(data);
    }

    @PutMapping(value = "/{id}" , consumes =  "multipart/form-data")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestParam("name") String name,
            @Valid @RequestParam("code") String code,
            @Valid @RequestParam("price") double price,
            @Valid @RequestParam("image") MultipartFile image,
            @Valid @RequestParam("category_id") Category category,
            @Valid @RequestParam("brand_id") Brand brand,
            @Valid @RequestParam("user_id") User user
    ) throws IOException {
        var product = productService.update(id,name,code,price,image,category,brand,user);
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Product updated successfully");
        data.put("product", product);
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var product  = productService.delete(id);
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Product deleted successfully");
        data.put("product", product);
        return ResponseEntity.ok(data);
    }
}
