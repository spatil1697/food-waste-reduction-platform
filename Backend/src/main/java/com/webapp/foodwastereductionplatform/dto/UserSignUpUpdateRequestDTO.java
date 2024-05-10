package com.webapp.foodwastereductionplatform.dto;

import com.webapp.foodwastereductionplatform.model.*;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSignUpUpdateRequestDTO {
        private String firstName;
        private String lastName;
        private String userType;
        @Embedded
        private Address address;
        private String contactNumber;
}
