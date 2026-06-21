package com.microservices.exception;

public class MessagingException extends RuntimeException {
    public MessagingException(String message) {
        super(message);
    }
}
