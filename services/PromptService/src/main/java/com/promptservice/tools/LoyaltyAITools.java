package com.promptservice.tools;


import com.airlineportal.client.LoyaltyFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Loyalty.LoyaltyAccountResponse;
import com.promptservice.service.AIContextService;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoyaltyAITools {
    private final LoyaltyFeignClient loyaltyFeignClient;
    private final AIContextService aiContextService;

    @Tool("""
        Retrieve the loyalty account of the currently authenticated user.
        Use this when the user asks about loyalty membership, available points,
        rewards, or their loyalty account.
        Never ask the user for a user ID or use an ID supplied in the message.
        """)
    public ApiResponse<LoyaltyAccountResponse> getMyLoyaltyAccount() {
        Long userId = aiContextService.getCurrentUserId();
        return loyaltyFeignClient.getAccountByUserId(userId);
    }


}
