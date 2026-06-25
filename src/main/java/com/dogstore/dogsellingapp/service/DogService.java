package com.dogstore.dogsellingapp.service;

import com.dogstore.dogsellingapp.dto.DogResponse;
import com.dogstore.dogsellingapp.model.Dog;
import com.dogstore.dogsellingapp.model.DogImage;
import com.dogstore.dogsellingapp.repository.DogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(readOnly = true)
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

    @Transactional
    public DogResponse createDog(String name, String breed, String location,
                                  BigDecimal price, String description,
                                  String gender, String age, String type,
                                  String microchipNumber, String sourceNumber, String sellerInfo,
                                  List<MultipartFile> images) throws IOException {
        Dog dog = Dog.builder()
                .name(name)
                .breed(breed)
                .location(location)
                .price(price)
                .description(description)
                .gender(gender)
                .age(age)
                .type(type)
                .microchipNumber(microchipNumber)
                .sourceNumber(sourceNumber)
                .sellerInfo(sellerInfo)
                .build();

        if (images != null) {
            for (MultipartFile image : images) {
                String imageUrl = saveImage(image);
                if (imageUrl != null) {
                    dog.getImages().add(DogImage.builder().imageUrl(imageUrl).dog(dog).build());
                }
            }
        }

        return toResponse(dogRepository.save(dog));
    }

    @Transactional
    public DogResponse updateDog(Long id, String name, String breed, String location,
                                  BigDecimal price, String description,
                                  String gender, String age, String type,
                                  String microchipNumber, String sourceNumber, String sellerInfo,
                                  List<MultipartFile> images) throws IOException {
        Dog dog = dogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dog not found with id: " + id));

        if (name != null) dog.setName(name);
        if (breed != null) dog.setBreed(breed);
        if (location != null) dog.setLocation(location);
        if (price != null) dog.setPrice(price);
        if (description != null) dog.setDescription(description);
        if (gender != null) dog.setGender(gender);
        if (age != null) dog.setAge(age);
        if (type != null) dog.setType(type);
        if (microchipNumber != null) dog.setMicrochipNumber(microchipNumber);
        if (sourceNumber != null) dog.setSourceNumber(sourceNumber);
        if (sellerInfo != null) dog.setSellerInfo(sellerInfo);
        if (images != null && !images.isEmpty()) {
            dog.getImages().clear();
            for (MultipartFile image : images) {
                String imageUrl = saveImage(image);
                if (imageUrl != null) {
                    dog.getImages().add(DogImage.builder().imageUrl(imageUrl).dog(dog).build());
                }
            }
        }

        return toResponse(dogRepository.save(dog));
    }

    @Transactional
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
        List<String> imageUrls = dog.getImages().stream()
                .map(DogImage::getImageUrl)
                .toList();
        return DogResponse.builder()
                .id(dog.getId())
                .name(dog.getName())
                .breed(dog.getBreed())
                .location(dog.getLocation())
                .price(dog.getPrice())
                .description(dog.getDescription())
                .gender(dog.getGender())
                .age(dog.getAge())
                .type(dog.getType())
                .microchipNumber(dog.getMicrochipNumber())
                .sourceNumber(dog.getSourceNumber())
                .sellerInfo(dog.getSellerInfo())
                .imageUrls(imageUrls)
                .createdAt(dog.getCreatedAt())
                .build();
    }
}
