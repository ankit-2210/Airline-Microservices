package com.promptservice.controller;


import com.promptservice.assistant.AirlineAssistant;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIChatController {
    private final AirlineAssistant airlineAssistant;

    @GetMapping("/test")
    public String testGemini(@RequestParam String message) {

        return airlineAssistant.chat("test-conversation", message);
    }


}
