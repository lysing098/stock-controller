package com.example.stockcontroller.service;

import com.example.stockcontroller.model.Role;
import com.example.stockcontroller.repository.RoleRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private  RoleRepository roleRepository;

    public Page<Role> getAllRoles(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if(search == null || search.isEmpty()) {
            return roleRepository.findAll(pageable);
        }
        return roleRepository.findByNameContainingIgnoreCase(search,pageable);
    }

    public Role create(@Valid Role role) {
        return roleRepository.save(role);
    }

    public Role update(Long id, @Valid Role role) {
        var role1 = roleRepository.findById(id).orElseThrow(()-> new RuntimeException("Role not found with id: " + id));
        role1.setName(role.getName());
        return roleRepository.save(role1);
    }

    public Role delete(Long id) {
        var role1 = roleRepository.findById(id).orElseThrow(()-> new RuntimeException("Role not found with id: " + id));
        roleRepository.delete(role1);
        return role1;
    }
}
