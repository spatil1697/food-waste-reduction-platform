package com.webapp.foodwastereductionplatform.model;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.persistence.*;
import lombok.*;
@AllArgsConstructor
@Embeddable
@Schema
@Data
@NoArgsConstructor
public class Address {

    @Column(
            name = "street_address"
    )
    private String streetAddress;
    private String city;
    private String state;
    @Column(
            name = "postal_code"
    )
    private String postalCode;
    private String country;
}
