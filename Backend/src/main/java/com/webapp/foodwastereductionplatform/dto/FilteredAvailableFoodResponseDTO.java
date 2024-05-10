package com.webapp.foodwastereductionplatform.dto;

import lombok.*;

import java.sql.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FilteredAvailableFoodResponseDTO {

    private String contactNumber;
    private String description;
    private String requestType;
    private String email;
    private String firstName;
    private String lastName;
    private String foodItem;
    private String location;
    private String status;
    private int quantity;
    private Timestamp creationDate;
}
