package com.airlineportal.logging;

public final class CorrelationId {
    public static final String HEADER_NAME = "X-Correlation-ID";
    public static final String MDC_KEY = "traceId";

    private CorrelationId() {

    }

}
