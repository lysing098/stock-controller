package com.example.stockcontroller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_name", columnNames = "user_name")
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "User name must not be empty!")
    @Size(min = 2, max = 20, message = "User name must be 2-20 characters")
    @Column(name = "user_name", nullable = false)
    private String userName;

    @NotBlank(message = "First name must not be empty!")
    @Size(min = 2, max = 20, message = "First name must be 2-20 characters")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name must not be empty!")
    @Size(min = 2, max = 20, message = "Last name must be 2-20 characters")
    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotBlank(message = "Tel must not be empty")
    @Column(nullable = false)
    private String tel;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email must not be empty")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false)
    private String password;

    private String address;

    private LocalDate dob;

    // stores filename/path of uploaded profile image
    private String image;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}