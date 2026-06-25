package com.dogstore.dogsellingapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String breed;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    private String gender;

    private String age;

    private String type;

    private String microchipNumber;

    private String sourceNumber;

    @Column(columnDefinition = "TEXT")
    private String sellerInfo;

    @OneToMany(mappedBy = "dog", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DogImage> images = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
