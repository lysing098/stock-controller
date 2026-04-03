package com.example.stockcontroller.controller;

import com.example.stockcontroller.dto.PaginateResponse;
import com.example.stockcontroller.model.Brand;
import com.example.stockcontroller.model.Category;
import com.example.stockcontroller.repository.BrandRepository;
import com.example.stockcontroller.service.BrandService;
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
@RequestMapping("/api/v1/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping()
    public ResponseEntity<?> getAllBrands(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<Brand> pageResult = brandService.getAllBrands(search,page,size);
        PaginateResponse<Brand> response = new PaginateResponse<>(
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
                                    @RequestParam("image")MultipartFile image
                                    ) throws IOException {

            var brand1 = brandService.create(name, image);
            HashMap<String, Object> response = new HashMap<>();
            response.put("message", "Brand created successfully");
            response.put("brand", brand1);

            return ResponseEntity.ok(response);


    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestParam("name") String name,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {

          var brand = brandService.update(id,name,image);
          HashMap<String, Object> response = new HashMap<>();
          response.put("message", "Brand updated successfully");
          response.put("brand", brand);
          return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var brand = brandService.delete(id);
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Brand deleted successfully");
        response.put("brand", brand);
        return ResponseEntity.ok(response);
    }


}
