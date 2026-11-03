package com.promptservice.service;

import com.promptservice.config.GeminiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeminiService {
    private final RestTemplate restTemplate;
    private final GeminiConfig geminiConfig;

    public String generateContent(String prompt) {
        String url = geminiConfig.getUrl() + "?key=" + geminiConfig.getApiKey();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "role", "user",
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                if (response.getBody() == null) {
                    throw new IllegalStateException("Empty response from Gemini.");
                }

                return response.getBody();
            }
            catch (RestClientException e) {
                log.error("Gemini API request failed. Attempt {}/3", attempt, e);
                if (attempt == 3) {
                    throw new RuntimeException("Gemini is temporarily unavailable.", e);
                }

                try {
                    Thread.sleep(1000L * attempt);
                }
                catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Gemini retry interrupted.", ex);
                }
            }
        }

        throw new RuntimeException("Gemini request failed.");
    }


}
