package com.airlineportal.client;


import com.airlineportal.payload.request.Fare.FareQuoteRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Fare.FareResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "FARESERVICE",
        path = "/internal/fares"
)
public interface FareFeignClient {

    @PostMapping("/quote")
    ApiResponse<FareResponse> getFareQuote(@Valid @RequestBody FareQuoteRequest request);


}
