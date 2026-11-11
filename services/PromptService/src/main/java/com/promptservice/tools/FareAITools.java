package com.promptservice.tools;

import com.airlineportal.client.FareFeignClient;
import com.airlineportal.payload.request.Fare.FareQuoteRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Fare.FareResponse;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FareAITools {
    private final FareFeignClient fareFeignClient;

    @Tool("""
        Calculate or retrieve a fare quote using the provided fare details.
        Use this when the user asks for a flight ticket price, fare estimate,
        or fare quote. Use the flight, passenger and other required details
        available from the conversation or retrieved flight information.
        Do not invent fare values.
        """)
    public ApiResponse<FareResponse> getFareQuote(FareQuoteRequest request) {
        return fareFeignClient.getFareQuote(request);
    }




}
