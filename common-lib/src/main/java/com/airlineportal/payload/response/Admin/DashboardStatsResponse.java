package com.airlineportal.payload.response.Admin;


import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {
    private long totalUsers;
    private long totalAirlines;
    private long totalFlights;
    private long totalBookings;
    private long pendingRefunds;
    private BigDecimal totalPaymentAmount = BigDecimal.ZERO;
    private BigDecimal totalRefundAmount = BigDecimal.ZERO;


}
