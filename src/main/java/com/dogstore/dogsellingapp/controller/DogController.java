package com.dogstore.dogsellingapp.controller;

import com.dogstore.dogsellingapp.dto.DogResponse;
import com.dogstore.dogsellingapp.service.DogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dogs")
@RequiredArgsConstructor
public class DogController {

    private final DogService dogService;

    @GetMapping
    public ResponseEntity<List<DogResponse>> getAllDogs() {
        return ResponseEntity.ok(dogService.getAllDogs());
    }

    @GetMapping("/search")
    public ResponseEntity<List<DogResponse>> searchDogs(@RequestParam(name = "q", required = false) String q) {
        return ResponseEntity.ok(dogService.searchDogs(q));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DogResponse> getDogById(@PathVariable Long id) {
        return ResponseEntity.ok(dogService.getDogById(id));
    }
}
