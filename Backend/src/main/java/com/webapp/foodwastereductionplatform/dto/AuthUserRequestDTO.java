package com.webapp.foodwastereductionplatform.dto;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthUserRequestDTO {
    private String jwt;
    private String message;
    private Boolean status;
}
