package com.loyaltyservice.dto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyReportData {
    private Long id;
    private Long userId;

    private String loyaltyTier;

    private Long totalPoints;
    private Long availablePoints;
    private Long lifetimePoints;

    private String createdAt;
    private String updatedAt;



}
