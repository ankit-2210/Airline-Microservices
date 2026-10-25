package com.airlineportal.payload.response.User;

public record RefreshResult(
        AuthResponse authResponse,
        String refreshToken
) {
}
