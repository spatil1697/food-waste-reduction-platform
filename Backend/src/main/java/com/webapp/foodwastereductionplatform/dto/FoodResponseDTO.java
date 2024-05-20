package com.webapp.foodwastereductionplatform.dto;

import lombok.*;

import java.sql.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodResponseDTO {

    private String requestType;
    private String contactNumber;
    private String email;
    private String firstName;
    private String lastName;
    private String foodItem;
    private String description;
    private int quantity;
    private String pickupLocation;
    private String status;
    private String imageBase64; // Base64-encoded image data
    private Timestamp creationDate;
}
