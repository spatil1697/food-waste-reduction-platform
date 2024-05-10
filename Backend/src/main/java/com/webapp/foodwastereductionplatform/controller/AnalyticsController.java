package com.webapp.foodwastereductionplatform.controller;

import com.webapp.foodwastereductionplatform.dto.*;
import com.webapp.foodwastereductionplatform.service.*;
import io.swagger.v3.oas.annotations.tags.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
@Tag( name = "Analytics" )
@AllArgsConstructor
public class AnalyticsController {
    private AnalyticsService analyticsService;

    @GetMapping
    public ResponseEntity<AnalyticsDTO> getAnalytics() {
        return ResponseEntity.ok(analyticsService.calculateAndSaveAnalytics());
    }
}


