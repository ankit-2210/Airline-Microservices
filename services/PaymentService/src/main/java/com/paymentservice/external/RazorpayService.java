package com.paymentservice.external;

import com.paymentservice.config.RazorpayConfig;
import com.paymentservice.dto.response.RazorpayOrderResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RazorpayService {
    private final RazorpayConfig razorpayConfig;
    private final RestClient restClient;

    // CREATE RAZORPAY ORDER
    public RazorpayOrderResponse createOrder(long amountInPaise, String receipt){
        String credentials = razorpayConfig.getKeyId() + ":" + razorpayConfig.getKeySecret();

        String encodedCredentials = Base64.getEncoder()
                        .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        Map<String, Object> request = Map.of(
                        "amount", amountInPaise,
                        "currency", razorpayConfig.getCurrency(),
                        "receipt", receipt);

        return restClient
                .post()
                .uri("/v1/orders")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(RazorpayOrderResponse.class);
    }


    // VERIFY PAYMENT SIGNATURE
    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature){
        try {
            String payload = orderId + "|" + paymentId;
            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec secretKey = new SecretKeySpec(razorpayConfig.getKeySecret()
                                    .getBytes(StandardCharsets.UTF_8), "HmacSHA256");

            mac.init(secretKey);
            byte[] digest = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

            String generatedSignature = Base64.getEncoder().encodeToString(digest);
            return generatedSignature.equals(signature);
        }
        catch (Exception e) {
            throw new IllegalStateException("Unable to verify Razorpay signature", e);
        }
    }



    public String getKeyId() {
        return razorpayConfig.getKeyId();
    }

}