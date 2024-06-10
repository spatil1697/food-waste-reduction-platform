package com.webapp.foodwastereductionplatform.service;

import com.webapp.foodwastereductionplatform.dto.*;
import com.webapp.foodwastereductionplatform.model.*;
import com.webapp.foodwastereductionplatform.repositories.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;
import org.springframework.web.multipart.*;

import java.io.*;
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
        validateUserId(userId);

        List<Food> foods = foodRepository.findByUserId(userId);
        validateFoodList(foods, "No food listings found for user ID: " + userId);

        return foods.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    public List<FoodResponseDTO> getAllFoodListings() {
        List<Food> foods = foodRepository.findAll();
        validateFoodList(foods, "No food listings found");

        List<Food> filteredListings = foods.stream()
                .filter(this::isListingActive)
                .collect(Collectors.toList());

        return filteredListings.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public FoodResponseDTO createFoodListing(FoodRequestDTO foodRequestDTO, Integer userId, MultipartFile imageFile) throws IOException {
        validateFoodRequest(foodRequestDTO);
        validateUserId(userId);
        validateQuantity(foodRequestDTO.getQuantity());

        Food food = Food.builder()
                .foodItem(foodRequestDTO.getFoodItem())
                .description(foodRequestDTO.getDescription())
                .quantity(foodRequestDTO.getQuantity())
                .expiryDate(foodRequestDTO.getExpiryDate())
                .pickupLocation(foodRequestDTO.getPickupLocation())
                .requestType(foodRequestDTO.getRequestType())
                .status(foodRequestDTO.getListingStatus())
                .creationDate(new Timestamp(System.currentTimeMillis()))
                .image(imageFile != null ? imageFile.getBytes() : null)
                .build();

        User user= getUserById(userId);
        food.setUser(user);
        foodRepository.save(food);
        return convertToDTO(food);
    }

    public FoodResponseDTO updateFoodListing(Integer userId, Integer foodId, FoodRequestDTO foodRequestDTO, MultipartFile imageFile) throws IOException {
        validateFoodRequest(foodRequestDTO);
        validateUserId(userId);
        validateQuantity(foodRequestDTO.getQuantity());
        getUserById(userId);

        Food existingFood = foodRepository.findById(foodId)
                .orElseThrow(() -> new EntityNotFoundException("Food listing with ID " + foodId + " not found"));
        // Check if the user is the owner of the food listing
        if (!existingFood.getUser().getId().equals(userId)) {
            throw new EntityNotFoundException("User with ID " + userId + " is not the owner of food listing with ID " + foodId);
        }

        existingFood.setFoodItem(foodRequestDTO.getFoodItem());
        existingFood.setDescription(foodRequestDTO.getDescription());
        existingFood.setQuantity(foodRequestDTO.getQuantity());
        existingFood.setExpiryDate(foodRequestDTO.getExpiryDate());
        existingFood.setPickupLocation(foodRequestDTO.getPickupLocation());
        existingFood.setStatus(foodRequestDTO.getListingStatus());
        existingFood.setRequestType(foodRequestDTO.getRequestType());
        if (imageFile != null) {
            existingFood.setImage(imageFile.getBytes());
        }

        foodRepository.save(existingFood);
        return convertToDTO(existingFood);
    }

    private void validateUserId(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Missing user ID");
        }
    }

    private void validateFoodList(List<Food> foods, String errorMessage) {
        if (ObjectUtils.isEmpty(foods)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
    }

    private boolean isListingActive(Food foodListing) {
        return !foodListing.getStatus().equals("claimed")
                && !foodListing.getStatus().equals("collected")
                && !foodListing.getStatus().equals("fulfilled")
                && (foodListing.getExpiryDate() == null || !foodListing.getExpiryDate().before(new Date()));
    }

    private FoodResponseDTO convertToDTO(Food food) {
        FoodResponseDTO foodResponseDTO = new FoodResponseDTO();

        if (food.getExpiryDate() != null && food.getExpiryDate().before(new Date()) && "foodlisting".equals(food.getRequestType())) {
            foodResponseDTO.setStatus("expired");
        } else {
            foodResponseDTO.setStatus(food.getStatus());
        }

        foodResponseDTO.setFoodItem(food.getFoodItem());
        foodResponseDTO.setDescription(food.getDescription());
        foodResponseDTO.setQuantity(food.getQuantity());
        foodResponseDTO.setPickupLocation(food.getPickupLocation());
        foodResponseDTO.setCreationDate(food.getCreationDate());
        foodResponseDTO.setRequestType(food.getRequestType());

        User user = food.getUser();
        if (user != null) {
            foodResponseDTO.setFirstName(user.getFirstName());
            foodResponseDTO.setLastName(user.getLastName());
            foodResponseDTO.setEmail(user.getEmail());
            foodResponseDTO.setContactNumber(user.getContactNumber());
        }

        byte[] imageData = food.getImage();
        if (imageData != null) {
            foodResponseDTO.setImageBase64(Base64.getEncoder().encodeToString(imageData));
        }

        return foodResponseDTO;
    }

    public void validateFoodRequest(FoodRequestDTO foodRequestDTO) {
        String requestType = foodRequestDTO.getRequestType().trim().toLowerCase().replace(" ", "");

        if (!requestType.equals("foodlisting") && !requestType.equals("foodrequest")) {
            throw new IllegalArgumentException("Invalid request type. Request Type must be 'foodlisting' or 'foodrequest'");
        }

        String listingStatus = foodRequestDTO.getListingStatus().toLowerCase();

        if (requestType.equals("foodlisting")) {
            if (foodRequestDTO.getExpiryDate() == null) {
                throw new IllegalArgumentException("Expiry date is required for food listing");
            }
            validateListingStatus(listingStatus, "available", "claimed", "collected", "expired");
        } else {
            validateListingStatus(listingStatus, "open", "fulfilled");
        }
    }
    
    private void validateListingStatus(String listingStatus, String... validStatuses) {
        if (!Arrays.asList(validStatuses).contains(listingStatus)) {
            throw new IllegalArgumentException("Invalid listing status. Listing status must be one of: " + String.join(", ", validStatuses));
        }
    }
}