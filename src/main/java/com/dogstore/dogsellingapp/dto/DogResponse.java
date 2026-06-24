package com.dogstore.dogsellingapp.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class DogResponse {
    private Long id;
    private String name;
    private String breed;
    private String location;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private LocalDateTime createdAt;
}
