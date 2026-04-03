package com.example.stockcontroller.controller;

import com.example.stockcontroller.dto.PaginateResponse;
import com.example.stockcontroller.model.Supplier;
import com.example.stockcontroller.repository.SupplierRepository;
import com.example.stockcontroller.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @GetMapping()
    public ResponseEntity<?> getAllSuppliers(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size" , defaultValue = "10") int size
    ) {
        Page<Supplier> pageResult = supplierService.getAllSuppliers(search,page,size);
        PaginateResponse<Supplier> response = new PaginateResponse<>(
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

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> create(
            @Valid @RequestParam("name") String name,
            @Valid @RequestParam("tel") String tel,
            @Valid @RequestParam("telegram") boolean telegram,
            @Valid @RequestParam("address") String address,
            @RequestParam("image" ) MultipartFile image
            ) throws IOException {

        var supplier1 = supplierService.create(name,tel,telegram,address,image);
        HashMap<String, Object> response = new HashMap<>();
        response.put("message","Supplier created successfully");
        response.put("supplier", supplier1);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestParam("name") String name,
            @Valid @RequestParam("tel") String tel,
            @Valid @RequestParam("telegram") boolean telegram,
            @Valid @RequestParam("address") String address,
            @RequestParam("image" ) MultipartFile image
    ) throws IOException {
        var supplier = supplierService.update(id,name,tel,telegram,address,image);
        HashMap<String, Object> response = new HashMap<>();
        response.put("message","Supplier updated successfully");
        response.put("supplier", supplier);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var supplier = supplierService.delete(id);
        HashMap<String, Object> response = new HashMap<>();
        response.put("message","Supplier deleted successfully");
        response.put("supplier", supplier);
        return ResponseEntity.ok(response);
    }
}
