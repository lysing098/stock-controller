package com.example.stockcontroller.dto.User;

import com.example.stockcontroller.model.Gender;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class UserRequest {

    @NotBlank(message = "User name must not be empty!")
    @Size(min = 2, max = 20, message = "User name must be 2-20 characters")
    private String userName;

    @NotBlank(message = "First name must not be empty!")
    @Size(min = 2, max = 20, message = "First name must be 2-20 characters")
    private String firstName;

    @NotBlank(message = "Last name must not be empty!")
    @Size(min = 2, max = 20, message = "Last name must be 2-20 characters")
    private String lastName;

    private Gender gender;

    @NotBlank(message = "Tel must not be empty")
    private String tel;

    @Email(message = "Email format invalid")
    @NotBlank(message = "Email must not be empty")
    private String email;

    @NotBlank(message = "Password must not be empty")
    private String password;

    private String address;

    private LocalDate dob;

    // image upload
    private MultipartFile image;

    // role reference
    @NotNull(message = "Role is required")
    private Long roleId;
}