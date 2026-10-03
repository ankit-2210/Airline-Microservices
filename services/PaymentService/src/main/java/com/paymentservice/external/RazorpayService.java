package com.paymentservice.external;

import com.paymentservice.config.RazorpayConfig;
import com.paymentservice.dto.response.RazorpayOrderResponse;
import com.paymentservice.dto.response.RazorpayPaymentLinkResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.security.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RazorpayService {
    private final RazorpayConfig razorpayConfig;
    private final RestClient restClient;

    // CREATE RAZORPAY ORDER
//    public RazorpayOrderResponse createOrder(long amountInPaise, String receipt){
//        String credentials = razorpayConfig.getKeyId() + ":" + razorpayConfig.getKeySecret();
//
//        String encodedCredentials = Base64.getEncoder()
//                        .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
//
//        Map<String, Object> request = Map.of(
//                        "amount", amountInPaise,
//                        "currency", razorpayConfig.getCurrency(),
//                        "receipt", receipt);
//
//        return restClient
//                .post()
//                .uri("/v1/orders")
//                .header(HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(request)
//                .retrieve()
//                .body(RazorpayOrderResponse.class);
//    }
//
//
//    // VERIFY PAYMENT SIGNATURE
//    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature){
//        try {
//            String payload = orderId + "|" + paymentId;
//            Mac mac = Mac.getInstance("HmacSHA256");
//
//            SecretKeySpec secretKey = new SecretKeySpec(razorpayConfig.getKeySecret()
//                                    .getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//
//            mac.init(secretKey);
//            byte[] digest = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
//
//            String generatedSignature = Base64.getEncoder().encodeToString(digest);
//            return generatedSignature.equals(signature);
//        }
//        catch (Exception e) {
//            throw new IllegalStateException("Unable to verify Razorpay signature", e);
//        }
//    }

    @PostConstruct
    public void validateConfig() {
        System.out.println("========== Razorpay Config ==========");

        System.out.println("Key ID present   : " + (razorpayConfig.getKeyId() != null && !razorpayConfig.getKeyId().isBlank()));
        System.out.println("Key ID prefix    : " + (razorpayConfig.getKeyId() != null &&
                                razorpayConfig.getKeyId().length() >= 8 ? razorpayConfig.getKeyId().substring(0, 8) : "INVALID"));
        System.out.println("Secret present   : " + (razorpayConfig.getKeySecret() != null && !razorpayConfig.getKeySecret().isBlank()));

        System.out.println("Webhook secret   : " + (razorpayConfig.getWebhookSecret() != null && !razorpayConfig.getWebhookSecret().isBlank()));
        System.out.println("Currency         : " + razorpayConfig.getCurrency());
        System.out.println("Base URL         : " + razorpayConfig.getBaseUrl());
        System.out.println("Callback URL     : " + razorpayConfig.getCallbackUrl());

        System.out.println("=====================================");
    }

    // Create Payment Link
    public RazorpayPaymentLinkResponse createPaymentLink(long amountInPaise, String referenceId, String description){

        Map<String, Object> request = new HashMap<>();

        request.put("amount", amountInPaise);
        request.put("currency", razorpayConfig.getCurrency());
        request.put("reference_id", referenceId);
        request.put("description", description);

        // Customer notification
        Map<String, Boolean> notify = new HashMap<>();
        notify.put("email", false);
        notify.put("sms", false);

        request.put("notify", notify);
        request.put("reminder_enable", false);

        // Callback
        request.put("callback_url", razorpayConfig.getCallbackUrl());
        request.put("callback_method", "get");

        return restClient
                .post()
                .uri(razorpayConfig.getBaseUrl() + "/v1/payment_links")
                .headers(headers ->
                        headers.setBasicAuth(
                                razorpayConfig.getKeyId(),
                                razorpayConfig.getKeySecret()
                        )
                )
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(RazorpayPaymentLinkResponse.class);

    }


    // VERIFY PAYMENT LINK CALLBACK SIGNATURE
    public boolean verifyPaymentLinkSignature(String paymentLinkId, String paymentLinkReferenceId, String paymentLinkStatus, String razorpayPaymentId, String razorpaySignature){
        try {
            if (paymentLinkId == null || paymentLinkReferenceId == null || paymentLinkStatus == null
                    || razorpayPaymentId == null || razorpaySignature == null || razorpayConfig.getKeySecret() == null) {
                return false;
            }

            String payload = paymentLinkId + "|" + paymentLinkReferenceId + "|" + paymentLinkStatus + "|" + razorpayPaymentId;

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(razorpayConfig.getKeySecret()
                                    .getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);

            byte[] digest = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            StringBuilder expectedSignature = new StringBuilder();
            for (byte b : digest) {
                expectedSignature.append(String.format("%02x", b));
            }

            return MessageDigest.isEqual(expectedSignature.toString()
                            .getBytes(StandardCharsets.UTF_8),
                    razorpaySignature.getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e) {
            throw new IllegalStateException("Unable to verify Razorpay payment link signature", e);
        }
    }

    // VERIFY WEBHOOK SIGNATURE
    public boolean verifyWebhookSignature(String payload, String razorpaySignature){
        try {
            if (payload == null || razorpaySignature == null || razorpayConfig.getWebhookSecret() == null || razorpayConfig.getWebhookSecret().isBlank()) {
                return false;
            }

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(razorpayConfig.getWebhookSecret()
                                    .getBytes(StandardCharsets.UTF_8), "HmacSHA256");

            mac.init(secretKey);
            byte[] digest = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            StringBuilder expectedSignature = new StringBuilder();

            for (byte b : digest) {
                expectedSignature.append(String.format("%02x", b));
            }

            return MessageDigest.isEqual(expectedSignature.toString()
                            .getBytes(StandardCharsets.UTF_8),
                    razorpaySignature.getBytes(StandardCharsets.UTF_8));

        }
        catch (Exception e){
            throw new IllegalStateException("Unable to verify Razorpay webhook signature", e);
        }
    }


    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }

    private boolean constantTimeEquals(String expected, String actual){
        if(expected == null || actual == null)
            return false;

        if(expected.length() != actual.length())
            return false;

        int result = 0;
        for(int i = 0; i < expected.length(); i++){
            result |= expected.charAt(i) ^ actual.charAt(i);
        }

        return result == 0;
    }

    public String getKeyId() {
        return razorpayConfig.getKeyId();
    }

}