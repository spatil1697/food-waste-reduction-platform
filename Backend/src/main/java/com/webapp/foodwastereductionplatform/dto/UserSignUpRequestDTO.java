package com.webapp.foodwastereductionplatform.dto;

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
}