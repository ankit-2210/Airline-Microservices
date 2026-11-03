package com.promptservice.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AIResponseMapper {
    private final ObjectMapper objectMapper;

    public <T> T map(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to map AI response.", e);
        }
    }

}
