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

    public FoodResponseDTO createFoodListing(FoodRequestDTO foodRequestDTO, Integer userId, MultipartFile imageFile) throws Exception {
        validateFoodRequest(foodRequestDTO, userId, imageFile);
        validateUserId(userId);
        validateQuantity(foodRequestDTO.getQuantity());
        validateImageFile(imageFile);

        Food food = Food.builder()
                .foodItem(foodRequestDTO.getFoodItem())
                .description(foodRequestDTO.getDescription())
                .quantity(foodRequestDTO.getQuantity())
                .expiryDate(foodRequestDTO.getExpiryDate())
                .pickupLocation(foodRequestDTO.getPickupLocation())
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
        validateFoodRequest(foodRequestDTO, userId, imageFile);
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

        if (imageFile != null) {
            existingFood.setImage(imageFile.getBytes());
        }

        foodRepository.save(existingFood);
        return convertToDTO(existingFood);
    }

    private void validateImageFile(MultipartFile imageFile) throws Exception {
        String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
        try {

            if(fileName.contains("..")) {
                throw  new Exception("Filename contains invalid path sequence " + fileName);
            }
             if (imageFile.getBytes().length > (1024 * 1024)) {
                throw new Exception("File size exceeds maximum limit");
            }
        } catch (MaxUploadSizeExceededException e) {
            throw new MaxUploadSizeExceededException(imageFile.getSize());
        } catch (Exception e) {
            throw new Exception("Could not save File: " + fileName);
        }
    }

    
    private FoodResponseDTO convertToDTO(Food food) {
        FoodResponseDTO foodResponseDTO = new FoodResponseDTO();

        if (food.getExpiryDate() != null && food.getExpiryDate().before(new Date())) {
            foodResponseDTO.setStatus("expired");
        } else {
            foodResponseDTO.setStatus(food.getStatus());
        }

        foodResponseDTO.setFoodItem(food.getFoodItem());
        foodResponseDTO.setDescription(food.getDescription());
        foodResponseDTO.setQuantity(food.getQuantity());
        foodResponseDTO.setPickupLocation(food.getPickupLocation());
        foodResponseDTO.setCreationDate(food.getCreationDate());

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

    private void validateFoodRequest(FoodRequestDTO foodRequestDTO, Integer userId, MultipartFile imageFile){
        if (isEmptyOrNull(foodRequestDTO.getFoodItem()) ||
        isEmptyOrNull(foodRequestDTO.getPickupLocation()) ||
        isEmptyOrNull(foodRequestDTO.getListingStatus()) ||
        foodRequestDTO.getQuantity() <= 0 ||
        foodRequestDTO.getExpiryDate() == null || userId == null || 
        userId == 0 || imageFile.isEmpty() || imageFile == null){
            throw new IllegalArgumentException("All fields except contact number must be present and cannot be empty.");
        }

        String listingStatus = foodRequestDTO.getListingStatus().toLowerCase();

        if (!listingStatus.equals("available") && 
            !listingStatus.equals("claimed") && 
            !listingStatus.equals("collected") && 
            !listingStatus.equals("expired")) {

                throw new IllegalArgumentException("Listing status must be one of the following: available, claimed, collected, expired.");
        }
    }

    private boolean isEmptyOrNull(String str) {
        return str == null || str.isEmpty();
    }
}