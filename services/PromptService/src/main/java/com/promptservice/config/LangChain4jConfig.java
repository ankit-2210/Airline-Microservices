package com.promptservice.config;

import com.promptservice.memory.PostgresChatMemoryStore;
import com.promptservice.assistant.AirlineAssistant;
import com.promptservice.tools.*;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LangChain4jConfig {
    private final PostgresChatMemoryStore memoryStore;

    private final LocationAITools locationAITools;
    private final FlightAITools flightAITools;
    private final FareAITools fareAITools;
    private final BookingAITools bookingAITools;
    private final SeatAITools seatAITools;
    private final RefundAITools refundAITools;
    private final CheckInAITools checkInAITools;
    private final BaggageAITools baggageAITools;
    private final UserAITools userAITools;
    private final LoyaltyAITools loyaltyAITools;
    private final PaymentAITools paymentAITools;

    @Bean
    public AirlineAssistant airlineAssistant(ChatModel geminiChatModel, PostgresChatMemoryStore memoryStore) {

        return AiServices.builder(AirlineAssistant.class)
                .chatModel(geminiChatModel)
                .tools(
                        locationAITools,
                        flightAITools,
                        fareAITools,
                        bookingAITools,
                        seatAITools,
                        refundAITools,
                        checkInAITools,
                        baggageAITools,
                        userAITools,
                        locationAITools,
                        paymentAITools
                )
                .chatMemoryProvider(memoryId ->
                        MessageWindowChatMemory.builder()
                                .id(memoryId)
                                .maxMessages(20)
                                .chatMemoryStore(memoryStore)
                                .build()
                )
                .build();
    }




}
