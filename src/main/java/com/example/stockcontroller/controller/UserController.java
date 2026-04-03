package com.example.stockcontroller.controller;

import com.example.stockcontroller.dto.PaginateResponse;
import com.example.stockcontroller.dto.User.UserRequest;
import com.example.stockcontroller.model.Role;
import com.example.stockcontroller.model.User;
import com.example.stockcontroller.repository.RoleRepository;
import com.example.stockcontroller.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        Page<User> pageResult = userService.getAllUsers(search, page, size);

        List<Role> roles = roleRepository.findAll();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("roles", roles);
        data.put("users", pageResult.getContent());

        List<Map<String, Object>> dataList = List.of(data);

        PaginateResponse<Map<String, Object>> response = new PaginateResponse<>(
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
            @Valid @ModelAttribute UserRequest request
    ) throws IOException {

        User user = userService.create(request);

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "User created successfully");
        response.put("user", user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @ModelAttribute UserRequest request
    ) throws IOException {

        User user = userService.update(id, request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User updated successfully");
        response.put("user", user);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        User user = userService.delete(id);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        response.put("user", user);

        return ResponseEntity.ok(response);
    }
}