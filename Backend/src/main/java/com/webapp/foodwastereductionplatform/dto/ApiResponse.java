package com.webapp.foodwastereductionplatform.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor

public class ApiResponse {
    private boolean success;
    private String message;
}
