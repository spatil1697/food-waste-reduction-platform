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
public class Analytics extends BaseEntity {

    private int metric;

    private double value;

    @Column( name = "creation_date")
    private Timestamp creationDate;

}
