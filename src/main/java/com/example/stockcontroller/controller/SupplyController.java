package com.example.stockcontroller.controller;

import com.example.stockcontroller.dto.PaginateResponse;
import com.example.stockcontroller.model.Supply;
import com.example.stockcontroller.service.SupplyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/supply")
public class SupplyController {

    @Autowired
    private SupplyService supplyService;

    @GetMapping()
    public ResponseEntity<?> getALlSupply(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<Supply> pageResult = supplyService.getAllSupply(page,search,size);
        PaginateResponse<Supply> response = new PaginateResponse<>(
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
    public ResponseEntity<?> create(@Valid @RequestBody Supply supply) {
        var supply1 = supplyService.create(supply);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Supply created successfully");
        response.put("supply", supply1);
        return ResponseEntity.ok(response);

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Supply supply) {
        var supply1  = supplyService.update(id,supply);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Supply updated successfully");
        response.put("supply", supply1);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var supply = supplyService.delete(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Supply deleted successfully");
        response.put("supply", supply);
        return ResponseEntity.ok(response);
    }
}
