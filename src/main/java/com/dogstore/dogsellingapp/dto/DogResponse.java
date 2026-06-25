package com.dogstore.dogsellingapp.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DogResponse {
    private Long id;
    private String name;
    private String breed;
    private String location;
    private BigDecimal price;
    private String description;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
}
