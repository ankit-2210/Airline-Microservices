package com.airlineportal.payload.request.Loyalty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedeemPointsRequest {
    @NotNull
    private Long userId;

    @NotNull
    @Min(1)
    private Long points;



}
