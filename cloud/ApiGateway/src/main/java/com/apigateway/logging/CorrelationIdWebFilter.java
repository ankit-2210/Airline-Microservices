package com.apigateway.logging;

import com.airlineportal.logging.CorrelationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CorrelationIdWebFilter implements WebFilter {
    private static final Logger log = LoggerFactory.getLogger(CorrelationIdWebFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String correlationId = exchange.getRequest()
                        .getHeaders()
                        .getFirst(CorrelationId.HEADER_NAME);

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        ServerHttpRequest request = exchange.getRequest()
                        .mutate()
                        .header(
                                CorrelationId.HEADER_NAME,
                                correlationId)
                        .build();

        ServerWebExchange mutatedExchange = exchange.mutate()
                        .request(request)
                        .build();

        mutatedExchange.getResponse()
                .getHeaders()
                .set(
                        CorrelationId.HEADER_NAME,
                        correlationId);

        String finalCorrelationId = correlationId;

        long start = System.currentTimeMillis();
        log.info(
                "Incoming request: method={}, path={}, correlationId={}",
                request.getMethod(),
                request.getURI().getPath(),
                finalCorrelationId
        );

        return chain.filter(mutatedExchange)
                .doFinally(signal -> {
                    long duration = System.currentTimeMillis() - start;
                    log.info(
                            "Request completed: method={}, path={}, status={}, durationMs={}, correlationId={}",
                            request.getMethod(),
                            request.getURI().getPath(),
                            mutatedExchange.getResponse()
                                    .getStatusCode(),
                            duration,
                            finalCorrelationId
                    );
                });
    }
}


//{
//        "service": "APIGATEWAY",
//        "level": "INFO",
//        "message": "Incoming request: method=POST, path=/api/bookings, correlationId=..."
//        }