package com.example.stockcontroller.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Supplier name must not be empty!")
    @Size(min = 2, max = 20, message = "Supplier name must be 2-20 characters")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Tel must not be blank")
    private String tel;

    private boolean telegram;

    @NotBlank(message = "Address can not blank")
    private String address;

    private String image;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
