package com.example.stockcontroller.controller;

import com.example.stockcontroller.dto.PaginateResponse;
import com.example.stockcontroller.dto.User.UserRequest;
import com.example.stockcontroller.dto.User.UserResponse;
import com.example.stockcontroller.model.Role;
import com.example.stockcontroller.model.User;
import com.example.stockcontroller.repository.RoleRepository;
import com.example.stockcontroller.service.CustomUserDetailsService;
import com.example.stockcontroller.service.UserService;
import com.example.stockcontroller.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

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

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody UserRequest userRequest) throws IOException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword())
        );

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(userRequest.getEmail());

        final String jwt = jwtUtil.generateToken(userDetails);

        String gender = (userRequest.getGender() != null)
                ? userRequest.getGender().name()
                : null;

        User user = userService.findByEmail(userRequest.getEmail());

        UserResponse userResponse = new UserResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getUserName(),
                user.getGender() != null ? user.getGender().name() : null,
                user.getTel(),
                user.getEmail(),
                user.getAddress(),
                user.getDob() != null ? user.getDob().toString() : null,
                user.getRole().getId()
        );

        return ResponseEntity.ok(new UserResponse(jwt, userResponse));
    }
}