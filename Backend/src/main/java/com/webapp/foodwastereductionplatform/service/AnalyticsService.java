package com.webapp.foodwastereductionplatform.service;

import com.webapp.foodwastereductionplatform.dto.*;
import com.webapp.foodwastereductionplatform.model.*;
import com.webapp.foodwastereductionplatform.repositories.*;
import lombok.*;
import org.springframework.stereotype.*;

import java.text.*;
import java.util.*;

@Service
@AllArgsConstructor
public class AnalyticsService {
    FoodRepository foodRepository;
    AnalyticsRepository analyticsRepository;

    public AnalyticsDTO calculateAndSaveAnalytics() {
        Analytics analytics = calculateAnalytics();
        analyticsRepository.save(analytics);
        return mapToDTO(analytics);
    }

    public Analytics calculateAnalytics() {

        List<Food> foods = foodRepository.findAll();
        int totalFood = foods.size();

        long collectedListingsCount = foods.stream()
                .filter(foodListing -> foodListing.getStatus().equals("collected"))
                .count();

        long fulfilledRequestsCount = foods.stream()
                .filter(foodRequest -> foodRequest.getStatus().equals("fulfilled"))
                .count();

        double overallSuccessPercentage = (double) (collectedListingsCount + fulfilledRequestsCount) / (double) totalFood * 100.0;

        if (Double.isNaN(overallSuccessPercentage)) {
            overallSuccessPercentage = 0.0;
        } else {
            DecimalFormat df = new DecimalFormat("#.##");
            overallSuccessPercentage = Double.parseDouble(df.format(overallSuccessPercentage));
        }

        Analytics analytics = new Analytics();
        analytics.setMetric(totalFood);
        analytics.setValue(overallSuccessPercentage);
        return analytics;
    }

    public AnalyticsDTO mapToDTO(Analytics analytics) {
        AnalyticsDTO analyticsDTO = new AnalyticsDTO();
        analyticsDTO.setMetric(analytics.getMetric());
        analyticsDTO.setValue(analytics.getValue());
        return analyticsDTO;
    }

}