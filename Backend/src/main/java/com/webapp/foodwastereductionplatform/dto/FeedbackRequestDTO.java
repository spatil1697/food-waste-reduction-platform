package com.webapp.foodwastereductionplatform.dto;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeedbackRequestDTO {

    private String feedbackType;
    private String Message;

}
