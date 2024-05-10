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
public class Feedback extends BaseEntity {

    private String feedbackType;

    private String message;

    @Column(
            name = "submission_date"
    )
    private Timestamp submissionDate;

    @ManyToOne
    @JoinColumn(
            name = "user_id"
    )
    private User user;
}
