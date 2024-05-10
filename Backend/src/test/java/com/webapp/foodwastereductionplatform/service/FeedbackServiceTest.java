package com.webapp.foodwastereductionplatform.service;

import com.webapp.foodwastereductionplatform.dto.*;
import com.webapp.foodwastereductionplatform.model.*;
import com.webapp.foodwastereductionplatform.repositories.*;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.*;

import java.util.*;

import static org.mockito.Mockito.*;

@SpringBootTest
class FeedbackServiceTest {
    @InjectMocks
    private FeedbackService feedbackService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FeedbackRepository feedbackRepository;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFeedback() throws Exception {
        // Prepare test data
        FeedbackRequestDTO feedbackRequestDTO = new FeedbackRequestDTO();
        feedbackRequestDTO.setFeedbackType("suggestion");
        feedbackRequestDTO.setMessage("Test suggestion message");
        Integer userId = 1;
        User user = new User();
        user.setId(userId);

        // Mocking repository response
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Invoking the method under test
        feedbackService.createFeedback(feedbackRequestDTO, userId);

        // Verifying repository method call
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void testCreateFeedbackWithInvalidType() {
        // Prepare test data
        FeedbackRequestDTO feedbackRequestDTO = new FeedbackRequestDTO();
        feedbackRequestDTO.setFeedbackType("invalid");
        Integer userId = 1;

        // Assertions
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // Invoking the method under test
            feedbackService.createFeedback(feedbackRequestDTO, userId);
        });
    }

    @Test
    void testCreateFeedbackWithNonExistingUser() {
        // Prepare test data
        FeedbackRequestDTO feedbackRequestDTO = new FeedbackRequestDTO();
        feedbackRequestDTO.setFeedbackType("suggestion");
        feedbackRequestDTO.setMessage("Test suggestion message");
        Integer userId = 1;

        // Mocking repository response
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Assertions
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            // Invoking the method under test
            feedbackService.createFeedback(feedbackRequestDTO, userId);
        });
    }
}