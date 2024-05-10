package com.webapp.foodwastereductionplatform.dto;


import com.webapp.foodwastereductionplatform.model.*;
import jakarta.persistence.Embedded;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSignUpRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userType;
    @Embedded
    private Address address;
    private String contactNumber;
}