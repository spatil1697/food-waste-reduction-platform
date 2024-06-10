package com.webapp.foodwastereductionplatform.controller;

import com.webapp.foodwastereductionplatform.dto.*;
import com.webapp.foodwastereductionplatform.service.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import java.util.*;

@RestController
@Tag(name = "Food")
@AllArgsConstructor
public class FoodController {
    private FoodService foodService;

    @PostMapping("/food/{userId}")
    public ResponseEntity<?> createFoodListing(@RequestBody FoodRequestDTO foodRequestDTO, @PathVariable Integer userId, @RequestParam("image") MultipartFile imageFile) {
        try {
            FoodResponseDTO createdFoodListing = foodService.createFoodListing(foodRequestDTO, userId, imageFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFoodListing);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/food/{userId}")
    public ResponseEntity<?> getAllFoodListingsByUser(@PathVariable Integer userId) {
        try {
            List<FoodResponseDTO> foodListings = foodService.getAllFoodListingsByUser(userId);
            return ResponseEntity.ok(foodListings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/food")
    public ResponseEntity<?> getAllFoodListings() {
        try {
            List<FoodResponseDTO> foodListings = foodService.getAllFoodListings();
            return ResponseEntity.ok(foodListings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }

    @PutMapping("/food/{userId}/{foodId}")
    public ResponseEntity<?> updateFoodListingByUser(@PathVariable Integer userId, @PathVariable Integer foodId, @RequestBody FoodRequestDTO foodRequestDTO, @RequestParam("image") MultipartFile imageFile) {
        try {
            FoodResponseDTO updatedFoodResponseDTO = foodService.updateFoodListing(userId, foodId, foodRequestDTO, imageFile);
            return ResponseEntity.ok(updatedFoodResponseDTO);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }
}