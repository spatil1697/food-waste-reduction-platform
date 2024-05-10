package com.webapp.foodwastereductionplatform.service;

import com.webapp.foodwastereductionplatform.dto.*;
import com.webapp.foodwastereductionplatform.model.*;
import com.webapp.foodwastereductionplatform.repositories.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.stream.*;

@Service
@AllArgsConstructor
public class FoodService {
    private FoodRepository foodRepository;
    private UserRepository userRepository;


    public List<FoodResponseDTO> getAllFoodListingsByUser(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Missing user ID");
        }

        List<Food> foods = foodRepository.findByUserId(userId);
        if (ObjectUtils.isEmpty(foods)) {
            throw new IllegalArgumentException("No food listings found for user ID: " + userId);
        }

        return foods.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public List<FoodResponseDTO> getAllFoodListings() {
        List<Food> foods = foodRepository.findAll();
        if (ObjectUtils.isEmpty(foods)) {
            throw new IllegalArgumentException("No food listings found");
        }

        List<Food> filteredListings = foods.stream()
                .filter(listing -> !isListingExpiredOrCompleted(listing))
                .collect(Collectors.toList());

        return filteredListings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public FoodResponseDTO createFoodListing(FoodRequestDTO foodRequestDTO, Integer userId) {
        // Check if the input parameters are not null
        if (foodRequestDTO == null || userId == null) {
            throw new IllegalArgumentException("Missing input data");
        }

        if (foodRequestDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        // Validate the food request
        validateFoodRequest(foodRequestDTO);

        // Build the Food object
        Food food = Food.builder()
                .foodItem(foodRequestDTO.getFoodItem())
                .description(foodRequestDTO.getDescription())
                .quantity(foodRequestDTO.getQuantity())
                .expiryDate(foodRequestDTO.getExpiryDate())
                .pickupLocation(foodRequestDTO.getPickupLocation())
                .requestType(foodRequestDTO.getRequestType())
                .status(foodRequestDTO.getListingStatus())
                .creationDate(new Timestamp(System.currentTimeMillis()))
                .build();

        // Check if the user exists
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User with ID " + userId + " not found");
        }

        // Assign the user to the food object and save
        User user = optionalUser.get();
        food.setUser(user);
        foodRepository.save(food);
        return convertToDTO(food);
    }


    public FoodResponseDTO updateFoodListing(Integer userId, Integer foodId, FoodRequestDTO foodRequestDTO) {
        // Check if input parameters are not null
        if (foodRequestDTO == null || userId == null || foodId == null) {
            throw new IllegalArgumentException("Missing input data");
        }

        if (foodRequestDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        // Retrieve user and food entities from repositories
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));

        Food existingFood = foodRepository.findById(foodId)
                .orElseThrow(() -> new EntityNotFoundException("Food listing with ID " + foodId + " not found"));

        // Check if the user is the owner of the food listing
        if (!existingFood.getUser().getId().equals(userId)) {
            throw new EntityNotFoundException("User with ID " + userId + " is not the owner of food listing with ID " + foodId);
        }

        // Validate food request
        validateFoodRequest(foodRequestDTO);

        // Update existing food entity with new data
        existingFood.setRequestType(foodRequestDTO.getRequestType());
        existingFood.setFoodItem(foodRequestDTO.getFoodItem());
        existingFood.setRequestType(foodRequestDTO.getRequestType());
        existingFood.setDescription(foodRequestDTO.getDescription());
        existingFood.setQuantity(foodRequestDTO.getQuantity());
        existingFood.setExpiryDate(foodRequestDTO.getExpiryDate());
        existingFood.setPickupLocation(foodRequestDTO.getPickupLocation());
        existingFood.setStatus(foodRequestDTO.getListingStatus());

        // Save updated food entity and convert to DTO
        Food updatedFood = foodRepository.save(existingFood);
        return convertToDTO(updatedFood);
    }

    private FoodResponseDTO convertToDTO(Food food) {
        FoodResponseDTO foodResponseDTO = new FoodResponseDTO();

        // Set status based on expiry date and request type
        if (food.getExpiryDate() != null && food.getExpiryDate().before(new Date()) && "foodlisting".equals(food.getRequestType())) {
            foodResponseDTO.setStatus("expired");
        } else {
            foodResponseDTO.setStatus(food.getStatus());
        }

        // Set food item, description, quantity, pickup location, and creation date
        foodResponseDTO.setFoodItem(food.getFoodItem());
        foodResponseDTO.setDescription(food.getDescription());
        foodResponseDTO.setQuantity(food.getQuantity());
        foodResponseDTO.setPickupLocation(food.getPickupLocation());
        foodResponseDTO.setCreationDate(food.getCreationDate());
        foodResponseDTO.setRequestType(food.getRequestType());

        // Set user-related fields if user is not null
        User user = food.getUser();
        if (user != null) {
            foodResponseDTO.setFirstName(user.getFirstName());
            foodResponseDTO.setLastName(user.getLastName());
            foodResponseDTO.setEmail(user.getEmail());
            foodResponseDTO.setContactNumber(user.getContactNumber());
        }

        return foodResponseDTO;
    }

    public void validateFoodRequest(FoodRequestDTO foodRequestDTO) {
        String requestType = foodRequestDTO.getRequestType().trim().toLowerCase().replace(" ", "");

        // Validate request type
        if (!requestType.equals("foodlisting") && !requestType.equals("foodrequest")) {
            throw new IllegalArgumentException("Invalid request type. Request Type must be foodlisting or foodrequest");
        }

        // Validate listing status based on request type
        String listingStatus = foodRequestDTO.getListingStatus().toLowerCase();
        if (requestType.equals("foodlisting")) {
            // For food listing, expiry date is required
            if (foodRequestDTO.getExpiryDate() == null) {
                throw new IllegalArgumentException("Expiry date is required for food listing");
            }
            // Validate listing status
            if (!Arrays.asList("available", "claimed", "collected", "expired").contains(listingStatus)) {
                throw new IllegalArgumentException("Invalid listing status. Listing status must be one of: available, claimed, collected, expired");
            }
        } else {
            // For food request, only "open" and "fulfilled" statuses are allowed
            if (!Arrays.asList("open", "fulfilled").contains(listingStatus)) {
                throw new IllegalArgumentException("Invalid listing status for food request");
            }
        }
    }
    private boolean isListingExpiredOrCompleted(Food foodListing) {
        return foodListing.getStatus().equals("claimed")
                || foodListing.getStatus().equals("collected")
                || foodListing.getStatus().equals("fulfilled")
                || (foodListing.getExpiryDate() != null && foodListing.getExpiryDate().before(new Date()));
    }
}