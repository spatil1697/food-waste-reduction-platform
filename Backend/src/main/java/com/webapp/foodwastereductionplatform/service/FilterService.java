package com.webapp.foodwastereductionplatform.service;

import com.webapp.foodwastereductionplatform.dto.*;
import com.webapp.foodwastereductionplatform.model.*;
import com.webapp.foodwastereductionplatform.repositories.*;
import lombok.*;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FilterService {

    private final FoodRepository foodRepository;

    public List<FilteredAvailableFoodResponseDTO> getAllFilteredFood(String filterRequestType) {
        if (filterRequestType == null || filterRequestType.isEmpty()) {
            throw new IllegalArgumentException("Missing filter request type");
        }

        String requestType = filterRequestType.trim().toLowerCase().replace(" ", "");

        if (!requestType.equals("foodlisting") && !requestType.equals("foodrequest")) {
            throw new IllegalArgumentException("Invalid request type. Request type must be foodlisting or foodrequest");
        }

        List<Food> availableFoods = foodRepository.findAll();
        List<Food> filteredFoodList = availableFoods.stream()
                .filter(food -> isValidFood(food, requestType))
                .collect(Collectors.toList());

        return filteredFoodList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private boolean isValidFood(Food food, String requestType) {
        if ("foodlisting".equals(requestType)) {
            return food.getRequestType().equals(requestType)
                    && !"claimed".equals(food.getStatus())
                    && !"collected".equals(food.getStatus())
                    && !"fulfilled".equals(food.getStatus())
                    && (food.getExpiryDate() == null || food.getExpiryDate().after(new Date()));
        } else {
            return food.getRequestType().equalsIgnoreCase(requestType)
                    && !"claimed".equals(food.getStatus())
                    && !"collected".equals(food.getStatus())
                    && !"fulfilled".equals(food.getStatus());
        }
    }

    private FilteredAvailableFoodResponseDTO convertToDTO(Food food) {
        FilteredAvailableFoodResponseDTO filteredAvailableFood = new FilteredAvailableFoodResponseDTO();

        if (food.getExpiryDate() != null && food.getExpiryDate().before(new Date()) && food.getRequestType().equals("foodlisting")) {
            filteredAvailableFood.setStatus("expired");
        } else {
            filteredAvailableFood.setStatus(food.getStatus());
        }

        filteredAvailableFood.setFoodItem(food.getFoodItem());
        filteredAvailableFood.setDescription(food.getDescription());
        filteredAvailableFood.setQuantity(food.getQuantity());
        filteredAvailableFood.setLocation(food.getPickupLocation());
        filteredAvailableFood.setCreationDate(food.getCreationDate());
        User user = food.getUser();
        filteredAvailableFood.setFirstName(user.getFirstName());
        filteredAvailableFood.setLastName(user.getLastName());
        filteredAvailableFood.setEmail(user.getEmail());
        filteredAvailableFood.setContactNumber(user.getContactNumber());

        return filteredAvailableFood;
    }
}


