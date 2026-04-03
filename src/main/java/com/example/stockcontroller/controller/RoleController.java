package com.example.stockcontroller.controller;

import com.example.stockcontroller.dto.PaginateResponse;
import com.example.stockcontroller.model.Role;
import com.example.stockcontroller.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping()
    public ResponseEntity<?> getAllRoles(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page" , defaultValue = "0") int page,
            @RequestParam(value = "size" , defaultValue = "10") int size
    ) {
        Page<Role> pageResult = roleService.getAllRoles(search,page,size);
        PaginateResponse<Role> response = new PaginateResponse<>(
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
    public ResponseEntity<?> create(@Valid @RequestBody Role role) {
        var role1 = roleService.create(role);
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Role created successfully");
        response.put("role", role1);
        return ResponseEntity.ok(response);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Role role) {
        var role1 = roleService.update(id,role);
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Role updated successfully");
        response.put("role", role1);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var role = roleService.delete(id);
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Role deleted successfully");
        response.put("role", role);
        return ResponseEntity.ok(response);
    }
}
