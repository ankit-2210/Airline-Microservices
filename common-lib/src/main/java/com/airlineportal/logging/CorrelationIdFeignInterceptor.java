package com.airlineportal.logging;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class CorrelationIdFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String correlationId = MDC.get(CorrelationId.MDC_KEY);
        if (correlationId != null && !correlationId.isBlank()) {
            template.header(CorrelationId.HEADER_NAME, correlationId);
        }

    }
}
