package com.dogstore.dogsellingapp.repository;

import com.dogstore.dogsellingapp.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {
	// Search by breed or location (case-insensitive, partial match)
	java.util.List<Dog> findByBreedContainingIgnoreCaseOrLocationContainingIgnoreCase(String breed, String location);
}
