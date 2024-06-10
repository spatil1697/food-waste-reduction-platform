package com.webapp.foodwastereductionplatform.controller;

import com.webapp.foodwastereductionplatform.dto.*;
import com.webapp.foodwastereductionplatform.service.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@Tag(name = "Feedback")
@AllArgsConstructor
public class FeedbackController {

    private FeedbackService feedbackService;

    @PostMapping("/{userId}")
    public ResponseEntity<String> createFeedback(@RequestBody FeedbackRequestDTO feedbackRequestDTO, @PathVariable Integer userId) {
        try {
            feedbackService.createFeedback(feedbackRequestDTO, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Feedback is saved successfully");
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }
}
