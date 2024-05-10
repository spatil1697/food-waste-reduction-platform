package com.webapp.foodwastereductionplatform.dto;

import com.webapp.foodwastereductionplatform.model.*;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthUserResponseDTO {
    private String jwt;
    private String message;
    private Boolean status;
    private String firstName;
    private String lastName;
    private String email;
    private String userType;
    private Address address;
    private String contactNumber;
}