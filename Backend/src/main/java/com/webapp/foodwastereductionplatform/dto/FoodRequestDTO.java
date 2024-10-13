package com.webapp.foodwastereductionplatform.dto;


import lombok.*;

import java.util.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodRequestDTO {

    private String requestType;
    private String foodItem;
    private String description;
    private int quantity;
    private Date expiryDate;
    private String pickupLocation;
    private String listingStatus;
    private String email;
    private String contactNumber;

}
