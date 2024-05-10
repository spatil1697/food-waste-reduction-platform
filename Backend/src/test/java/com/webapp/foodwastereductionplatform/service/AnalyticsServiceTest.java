package com.webapp.foodwastereductionplatform.service;

import com.webapp.foodwastereductionplatform.dto.*;
import com.webapp.foodwastereductionplatform.model.*;
import com.webapp.foodwastereductionplatform.repositories.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.*;

import java.sql.*;
import java.util.*;
import java.util.Date;

@SpringBootTest
class AnalyticsServiceTest {
    @InjectMocks
    private AnalyticsService analyticsService;
    @Mock
    private FoodRepository foodRepository;
    @Mock
    private AnalyticsRepository analyticsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCalculateAndSaveAnalytics() {
        Timestamp creationDate = new Timestamp(System.currentTimeMillis());
        Address address = new Address("street 123",
                "Berlin",
                "Berlin",
                "10115",
                "Germany");

        User user = new User("Sam",
                "Rockson",
                "sm@gmail.com",
                "ramp",
                "individual",
                address,
                "+40 12114234234");

        List<Food> foods = new ArrayList<>();
        foods.add(new Food("food Request",
                "Burger",
                "Description",
                1,
                new java.util.Date(),
                "Location",
                "collected",
                creationDate, user));

        foods.add(new Food("food Request",
                "Burger",
                "Description",
                1,
                new Date(),
                "Location",
                "collected",
                creationDate,
                user));

        // Mocking repository response
        Mockito.when(foodRepository.findAll()).thenReturn(foods);

        // Invoking the method under test
        AnalyticsDTO result = analyticsService.calculateAndSaveAnalytics();

        // Assertions
        Assertions.assertEquals(2, result.getMetric());
        Assertions.assertEquals(100.0, result.getValue());

        // Verifying repository method call
        Mockito.verify(analyticsRepository, Mockito.times(1)).save(Mockito.any(Analytics.class));
    }
}
