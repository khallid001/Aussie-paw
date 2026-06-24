package com.dogstore.dogsellingapp.service;

import com.dogstore.dogsellingapp.dto.DogResponse;
import com.dogstore.dogsellingapp.model.Dog;
import com.dogstore.dogsellingapp.repository.DogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DogService {

    private final DogRepository dogRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public List<DogResponse> getAllDogs() {
        return dogRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<DogResponse> searchDogs(String query) {
        if (query == null || query.isBlank()) {
            return getAllDogs();
        }
        var dogs = dogRepository.findByBreedContainingIgnoreCaseOrLocationContainingIgnoreCase(query, query);
        return dogs.stream().map(this::toResponse).toList();
    }

    public DogResponse getDogById(Long id) {
        Dog dog = dogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dog not found with id: " + id));
        return toResponse(dog);
    }

    public DogResponse createDog(String name, String breed, String location,
                                  BigDecimal price, String description, MultipartFile image) throws IOException {
        String imageUrl = saveImage(image);

        Dog dog = Dog.builder()
                .name(name)
                .breed(breed)
                .location(location)
                .price(price)
                .description(description)
                .imageUrl(imageUrl)
                .build();

        return toResponse(dogRepository.save(dog));
    }

    public DogResponse updateDog(Long id, String name, String breed, String location,
                                  BigDecimal price, String description, MultipartFile image) throws IOException {
        Dog dog = dogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dog not found with id: " + id));

        if (name != null) dog.setName(name);
        if (breed != null) dog.setBreed(breed);
        if (location != null) dog.setLocation(location);
        if (price != null) dog.setPrice(price);
        if (description != null) dog.setDescription(description);
        if (image != null && !image.isEmpty()) {
            dog.setImageUrl(saveImage(image));
        }

        return toResponse(dogRepository.save(dog));
    }

    public void deleteDog(Long id) {
        if (!dogRepository.existsById(id)) {
            throw new IllegalArgumentException("Dog not found with id: " + id);
        }
        dogRepository.deleteById(id);
    }

    private String saveImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            return null;
        }
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + filename;
    }

    private DogResponse toResponse(Dog dog) {
        return DogResponse.builder()
                .id(dog.getId())
                .name(dog.getName())
                .breed(dog.getBreed())
                .location(dog.getLocation())
                .price(dog.getPrice())
                .description(dog.getDescription())
                .imageUrl(dog.getImageUrl())
                .createdAt(dog.getCreatedAt())
                .build();
    }
}
