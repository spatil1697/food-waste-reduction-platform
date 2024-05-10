package com.webapp.foodwastereductionplatform.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.*;

import java.util.*;


@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@SuperBuilder
@Entity(name ="_user")
public class User extends BaseEntity {

    @Column(
            name = "first_name"
    )
    private String firstName;

    @Column(
            name = "last_name"
    )
    private String lastName;

    @Column(
            unique = true,
            nullable = false
    )
    private String email;

    private String password;

    @Column(
            name = "user_type"
    )
    private String userType;

    @Column(
            name = "contact_number"
    )
    private String contactNumber;

    @Embedded
    private Address address;

    @OneToMany(
            mappedBy = "user"
    )
    private List<Food> foods;

    @OneToMany(
            mappedBy = "user"
    )
    private List<Donation> donations;

    @OneToMany(
            mappedBy = "user"
    )
    private List<Feedback> feedbacks;

    public User(String firstName, String lastName, String email, String password, String userType, Address address, String contactNumber) {
    }
}
