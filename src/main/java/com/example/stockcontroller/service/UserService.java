package com.example.stockcontroller.service;

import com.example.stockcontroller.config.FileImageService;
import com.example.stockcontroller.dto.User.UserRequest;
import com.example.stockcontroller.dto.User.UserResponse;
import com.example.stockcontroller.exception.MyResourceNotFoundException;
import com.example.stockcontroller.model.Gender;
import com.example.stockcontroller.model.Role;
import com.example.stockcontroller.model.User;
import com.example.stockcontroller.repository.RoleRepository;
import com.example.stockcontroller.repository.UserRepository;
import com.example.stockcontroller.util.JwtUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileImageService fileImageService;
    @Autowired
    private RoleRepository roleRepository;



    public Page<User> getAllUsers(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if(search == null || search.isEmpty()) {
            return userRepository.findAll(pageable);
        }
        return userRepository.findByUserNameContainingIgnoreCase(search,pageable);
    }

    public User create(UserRequest request) throws IOException {

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new MyResourceNotFoundException("Role not found with id " + request.getRoleId()));

        String profileName = null;

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            profileName = fileImageService.uploadImage(request.getImage());
        }

        User user = new User();
        user.setUserName(request.getUserName());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setTel(request.getTel());
        user.setEmail(request.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        user.setAddress(request.getAddress());
        user.setDob(request.getDob());
        user.setImage(profileName);
        user.setRole(role);

        return userRepository.save(user);
    }

    public User update(Long id, UserRequest request) throws IOException {

        User findUser = userRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("User not found with id " + id));

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new MyResourceNotFoundException("Role not found with id " + request.getRoleId()));

        if (request.getImage() != null && !request.getImage().isEmpty()) {

            if (findUser.getImage() != null && !findUser.getImage().isEmpty()) {
                fileImageService.deleteImage(findUser.getImage());
            }

            String profileName = fileImageService.uploadImage(request.getImage());
            findUser.setImage(profileName);
        }

        findUser.setUserName(request.getUserName());
        findUser.setFirstName(request.getFirstName());
        findUser.setLastName(request.getLastName());
        findUser.setGender(request.getGender());
        findUser.setTel(request.getTel());
        findUser.setEmail(request.getEmail());
        findUser.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        findUser.setAddress(request.getAddress());
        findUser.setDob(request.getDob());
        findUser.setRole(role);

        return userRepository.save(findUser);
    }

    public User delete(Long id) {
        var findUser = userRepository.findById(id).orElseThrow(() -> new MyResourceNotFoundException("User not found with id " + id));
        if(findUser.getImage() != null && !findUser.getImage().isEmpty()) {
            fileImageService.deleteImage(findUser.getImage());
        }
        userRepository.deleteById(id);
        return findUser;
    }


   public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new MyResourceNotFoundException("User not found with email " + email));
   }
}
