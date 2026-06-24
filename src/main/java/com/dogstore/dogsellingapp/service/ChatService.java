package com.dogstore.dogsellingapp.service;

import com.dogstore.dogsellingapp.model.Dog;
import com.dogstore.dogsellingapp.repository.DogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final DogRepository dogRepository;

    private static final String[] BREED_KEYWORDS = {
            "golden retriever", "cavoodle", "french bulldog", "labrador",
            "german shepherd", "groodle", "border collie", "poodle",
            "bulldog", "beagle", "rottweiler", "husky", "corgi",
            "dachshund", "pug", "maltese", "shih tzu", "chihuahua"
    };

    private static final String[] PUPPY_KEYWORDS = {
            "puppy", "puppies", "available", "have", "dog", "breed", "breeds", "show me"
    };

    private static final String[] CONTACT_KEYWORDS = {
            "contact", "email", "address", "location", "where", "reach", "phone"
    };

    private static final String[] PRICE_KEYWORDS = {
            "price", "cost", "how much", "expensive", "cheap"
    };

    public String reply(String userMessage) {
        if (userMessage == null || userMessage.isBlank()) {
            return "Hi! I'm the AussiePaw Assistant \uD83D\uDC3E Ask me about available puppies, pricing, or how to contact us!";
        }
        String msg = userMessage.toLowerCase();

        // 1. BREED/PUPPY INQUIRY
        String detectedBreed = extractBreed(msg);
        if (detectedBreed != null) {
            return handleBreedInquiry(detectedBreed);
        }
        if (containsAny(msg, PUPPY_KEYWORDS)) {
            return handleGeneralPuppyInquiry();
        }

        // 2. CONTACT/LOCATION INQUIRY
        if (containsAny(msg, CONTACT_KEYWORDS)) {
            return "You can reach us at Support@aussiepaw.dog or  call us on +61 489 980 105 or  visit us at 11 Leeds St, Rhodes NSW 2138, Australia. We'd love to hear from you!";
        }

        // 3. PRICE INQUIRY
        if (containsAny(msg, PRICE_KEYWORDS)) {
            return handlePriceInquiry();
        }

        // 4. DEFAULT
        return "Hi! I'm the AussiePaw Assistant \uD83D\uDC3E Ask me about available puppies, pricing, or how to contact us!";
    }

    private String handleBreedInquiry(String breed) {
        List<Dog> matches = dogRepository.findByBreedContainingIgnoreCaseOrLocationContainingIgnoreCase(breed, breed);
        if (matches.isEmpty()) {
            return "We don't have any " + breed + " puppies available right now, but new puppies arrive regularly! Contact us at Support@aussiepaw.dog to join the waiting list.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("We have ").append(matches.size()).append(" ").append(breed)
                .append(matches.size() == 1 ? " puppy" : " puppies")
                .append(" available! Here's what we have: ");
        for (int i = 0; i < matches.size(); i++) {
            Dog d = matches.get(i);
            if (i > 0) sb.append(", ");
            sb.append(d.getName()).append(" in ").append(d.getLocation())
                    .append(" for $").append(d.getPrice());
        }
        sb.append(". Contact us to arrange a visit!");
        return sb.toString();
    }

    private String handleGeneralPuppyInquiry() {
        List<Dog> allDogs = dogRepository.findAll();
        if (allDogs.isEmpty()) {
            return "We don't have any puppies listed right now, but new puppies arrive regularly! Contact us at Support@aussiepaw.dog to join the waiting list.";
        }
        long breedCount = allDogs.stream().map(d -> d.getBreed().toLowerCase()).distinct().count();
        return "We currently have " + allDogs.size() + " puppies across " + breedCount + " breeds available! Ask me about a specific breed like 'Golden Retriever' or 'Cavoodle' to see details.";
    }

    private String handlePriceInquiry() {
        List<Dog> allDogs = dogRepository.findAll();
        if (allDogs.isEmpty()) {
            return "We don't have any puppies listed right now. Contact us at Support@aussiepaw.dog or call us on : +61489980105  for upcoming availability!";
        }
        BigDecimal min = allDogs.stream().map(Dog::getPrice).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal max = allDogs.stream().map(Dog::getPrice).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        return "Our puppies are priced between $" + min + " and $" + max + " depending on the breed. Contact us at Support@aussiepaw.dog for exact pricing!";
    }

    private String extractBreed(String msg) {
        for (String breed : BREED_KEYWORDS) {
            if (msg.contains(breed)) {
                return breed;
            }
        }
        return null;
    }

    private boolean containsAny(String msg, String[] keywords) {
        for (String kw : keywords) {
            if (msg.contains(kw)) return true;
        }
        return false;
    }
}
