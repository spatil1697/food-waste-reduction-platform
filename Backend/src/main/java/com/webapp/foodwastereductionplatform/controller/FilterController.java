package com.webapp.foodwastereductionplatform.controller;

import com.webapp.foodwastereductionplatform.dto.*;
import com.webapp.foodwastereductionplatform.service.*;
import io.swagger.v3.oas.annotations.tags.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/*@RestController
@Tag(name = "Filtered Food Requests and Listings")
@AllArgsConstructor
public class FilterController {
    private FilterService filterService;


    @GetMapping("/filtered-food/{requestType}")
    public ResponseEntity<?> getAllFilteredFoodByRequestType(@PathVariable String requestType) {
        try {
            List<FilteredAvailableFoodResponseDTO> filteredAvailableFood = filterService.getAllFilteredFood(requestType);
            return ResponseEntity.ok(filteredAvailableFood);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }
}*/
