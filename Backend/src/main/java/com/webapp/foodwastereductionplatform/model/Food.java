package com.webapp.foodwastereductionplatform.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.*;

import java.sql.*;
import java.util.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@SuperBuilder
@Entity
public class Food extends BaseEntity {

    @Column(name = "request_type")
    private String requestType;

    @Column(name = "food_item")
    private String foodItem;

    private String description;
    private int quantity;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "pickup_location")
    private String pickupLocation;

    private String status;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
