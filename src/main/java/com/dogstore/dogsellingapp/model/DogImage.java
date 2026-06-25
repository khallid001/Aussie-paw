package com.dogstore.dogsellingapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dog_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DogImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id", nullable = false)
    private Dog dog;
}
