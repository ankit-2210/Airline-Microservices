package com.promptservice.tools;

import com.airlineportal.client.UserFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.User.UserResponse;
import com.promptservice.service.AIContextService;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAITools {
    private final UserFeignClient userFeignClient;
    private final AIContextService aiContextService;

    @Tool("""
        Retrieve the profile of the currently authenticated user.
        Use this when the user asks about their account or profile information.
        Never request or accept a user ID from the user.
        Use the authenticated application context to identify the user.
        """)
    public ApiResponse<UserResponse> getMyProfile() {
        Long userId = aiContextService.getCurrentUserId();
        return userFeignClient.getUserById(userId);
    }


}
