package com.webapp.foodwastereductionplatform.service;

import com.webapp.foodwastereductionplatform.dto.*;
import com.webapp.foodwastereductionplatform.model.*;
import com.webapp.foodwastereductionplatform.repositories.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.*;

import java.sql.*;
import java.time.*;
import java.util.*;

@SpringBootTest
class FoodServiceTest {
    @Mock
    private FoodRepository foodRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private FoodService foodService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateFoodListing() throws Exception {
        // Prepare test data
        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setRequestType("food Listing");
        foodRequestDTO.setFoodItem("Test Food");
        foodRequestDTO.setDescription("Test Description");
        foodRequestDTO.setQuantity(1);
        foodRequestDTO.setExpiryDate(Timestamp.from(Instant.now()));
        foodRequestDTO.setPickupLocation("Test Location");
        foodRequestDTO.setListingStatus("available");
        Integer userId = 1;
        User user = new User();
        user.setId(userId);

        // Mocking repository response
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Invoke the method under test
        foodService.createFoodListing(foodRequestDTO, userId);

        // Verify repository method call
        Mockito.verify(foodRepository, Mockito.times(1)).save(Mockito.any(Food.class));
    }

    @Test
    void testUpdateFoodListing() throws Exception {
        // Prepare test data
        Integer userId = 1;
        Integer foodId = 1;
        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setRequestType("food Listing");
        foodRequestDTO.setFoodItem("Test Food");
        foodRequestDTO.setDescription("Test Description");
        foodRequestDTO.setQuantity(1);
        foodRequestDTO.setExpiryDate(Timestamp.from(Instant.now()));
        foodRequestDTO.setPickupLocation("Test Location");
        foodRequestDTO.setListingStatus("available");
        User user = new User();
        user.setId(userId);
        Food food = new Food();
        food.setId(foodId);
        food.setUser(user);

        // Mocking repository response
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));
        Mockito.when(foodRepository.save(Mockito.any(Food.class))).thenReturn(food);

        // Invoke the method under test
        foodService.updateFoodListing(userId, foodId, foodRequestDTO);

        // Verify repository method call
        Mockito.verify(foodRepository, Mockito.times(1)).save(Mockito.any(Food.class));
    }

    @Test
    void testGetAllFoodListingsByUser() {
        // Prepare test data
        Integer userId = 1;
        List<Food> foods = new ArrayList<>();
        foods.add(new Food());
        foods.add(new Food());

        // Mocking repository response
        Mockito.when(foodRepository.findByUserId(userId)).thenReturn(foods);

        // Invoke the method under test
        List<FoodResponseDTO> foodResponseDTOs = foodService.getAllFoodListingsByUser(userId);

        // Assertions
        Assertions.assertNotNull(foodResponseDTOs);
        Assertions.assertEquals(2, foodResponseDTOs.size());
    }

    @Test
    void testGetAllFoodListings() {
        // Prepare test data
        List<Food> foods = new ArrayList<>();
        Food food1 = new Food();
        food1.setRequestType("food listing");
        food1.setFoodItem("Test Food 1");
        food1.setDescription("Description for Test Food 1");
        food1.setQuantity(1);
        food1.setExpiryDate(Timestamp.from(Instant.now().plus(Duration.ofDays(10L))));
        food1.setStatus("available");

        Food food2 = new Food();
        food2.setRequestType("food listing");
        food2.setFoodItem("Test Food 2");
        food2.setDescription("Description for Test Food 2");
        food2.setQuantity(2);
        food2.setExpiryDate(Timestamp.from(Instant.now().plus(Duration.ofDays(10L))));
        food2.setStatus("claimed");

        foods.add(food1);
        foods.add(food2);

        // Mocking repository response
        Mockito.when(foodRepository.findAll()).thenReturn(foods);
        Mockito.when(foodRepository.saveAll(Mockito.anyList())).thenReturn(foods);

        // Invoke the method under test
        List<FoodResponseDTO> foodResponseDTOs = foodService.getAllFoodListings();

        // Assertions
        Assertions.assertNotNull(foodResponseDTOs);
        Assertions.assertEquals(1, foodResponseDTOs.size());
    }
}
