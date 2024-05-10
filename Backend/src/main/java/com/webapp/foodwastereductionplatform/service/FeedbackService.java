package com.webapp.foodwastereductionplatform.service;

import com.webapp.foodwastereductionplatform.dto.*;
import com.webapp.foodwastereductionplatform.model.*;
import com.webapp.foodwastereductionplatform.repositories.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.*;

import java.sql.*;
import java.util.*;

@Service
@AllArgsConstructor
public class FeedbackService {
    private UserRepository userRepository;
    private FeedbackRepository feedbackRepository;

    public void createFeedback(FeedbackRequestDTO feedbackRequestDTO, Integer userId) throws Exception {
        String feedbackType = feedbackRequestDTO.getFeedbackType().toLowerCase();

        if (!feedbackType.equals("suggestion") && !feedbackType.equals("issue") && !feedbackType.equals("compliment")) {
            throw new IllegalArgumentException("Feedback type must be suggestion, issue, or compliment");
        }

        Feedback feedback = Feedback.builder()
                                    .feedbackType(feedbackType)
                                    .message(feedbackRequestDTO.getMessage())
                                    .submissionDate(new Timestamp(System.currentTimeMillis()))
                                    .build();


        Optional<User> optionalUser = userRepository.findById(userId);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                feedback.setUser(user);
                feedbackRepository.save(feedback);
            } else {
                throw new EntityNotFoundException("User with ID " + userId + " not found");
            }
        }
}
