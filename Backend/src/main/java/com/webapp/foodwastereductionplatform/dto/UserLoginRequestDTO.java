package com.webapp.foodwastereductionplatform.dto;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginRequestDTO {
    private String email;
    private String password;
}
