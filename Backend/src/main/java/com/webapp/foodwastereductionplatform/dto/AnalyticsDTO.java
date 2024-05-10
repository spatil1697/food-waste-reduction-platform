package com.webapp.foodwastereductionplatform.dto;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnalyticsDTO {

    private int metric;
    private double value;
}
