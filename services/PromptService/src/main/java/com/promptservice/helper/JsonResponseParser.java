package com.promptservice.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JsonResponseParser {
    private final ObjectMapper objectMapper;

    public String extractJson(String aiResponse) {
        try {
            JsonNode root = objectMapper.readTree(aiResponse);

            JsonNode candidates = root.path("candidates");

            if (!candidates.isArray() || candidates.isEmpty()) {
                throw new IllegalArgumentException("Gemini returned no candidates.");
            }

            StringBuilder result = new StringBuilder();
            for (JsonNode part : candidates.get(0)
                    .path("content")
                    .path("parts")) {

                if (part.has("text")) {
                    result.append(part.get("text").asText());
                }
            }

            String text = result.toString().trim();
            if (text.startsWith("```")) {
                text = text.replaceFirst(
                        "^```(?:json)?\\s*", ""
                ).replaceFirst("\\s*```$", "");
            }

            if (text.isBlank()) {
                throw new IllegalArgumentException("Gemini returned empty content.");
            }

            return text.trim();

        }
        catch (Exception e) {
            throw new RuntimeException("Unable to extract Gemini response.", e);
        }
    }


}
