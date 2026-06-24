package com.dogstore.dogsellingapp.controller;

import com.dogstore.dogsellingapp.dto.DogResponse;
import com.dogstore.dogsellingapp.dto.MessageResponse;
import com.dogstore.dogsellingapp.service.DogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/admin/dogs")
@RequiredArgsConstructor
public class AdminDogController {

    private final DogService dogService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DogResponse> createDog(
            @RequestParam String name,
            @RequestParam String breed,
            @RequestParam String location,
            @RequestParam BigDecimal price,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile image) throws IOException {
        return ResponseEntity.ok(dogService.createDog(name, breed, location, price, description, image));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DogResponse> updateDog(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile image) throws IOException {
        return ResponseEntity.ok(dogService.updateDog(id, name, breed, location, price, description, image));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteDog(@PathVariable Long id) {
        dogService.deleteDog(id);
        return ResponseEntity.ok(new MessageResponse("Dog deleted successfully."));
    }
}
