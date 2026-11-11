package com.promptservice.controller;

import com.airlineportal.payload.request.AI.AIChatRequest;
import com.airlineportal.payload.response.AI.AIChatResponse;
import com.airlineportal.payload.response.ApiResponse;
import com.promptservice.assistant.AirlineAssistant;
import com.promptservice.service.AIChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIChatController {
    private final AIChatService aiChatService;

    @PostMapping("/chat")
    public ApiResponse<AIChatResponse> chat(@Valid @RequestBody AIChatRequest request) {
        return ApiResponse.success(aiChatService.chat(request));
    }



}
