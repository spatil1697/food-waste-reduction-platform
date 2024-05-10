package com.webapp.foodwastereductionplatform.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.*;

import java.sql.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@SuperBuilder
@Entity
public class Donation extends BaseEntity{

    @Column(
            name = "donation_status"
    )
    private String donationStatus;

    @Column(
            name = "creation_date"
    )
    private Timestamp creationDate;

    @ManyToOne
    @JoinColumn(
            name = "food_listing_id"
    )
    private Food food;

    @ManyToOne
    @JoinColumn(
            name = "user_id"
    )
    private User user;

}
