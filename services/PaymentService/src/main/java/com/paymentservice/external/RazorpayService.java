package com.paymentservice.external;

import com.paymentservice.config.RazorpayConfig;
import com.paymentservice.dto.response.RazorpayOrderResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RazorpayService {
    private final RazorpayConfig razorpayConfig;

    // CREATE RAZORPAY ORDER
    public RazorpayOrderResponse createOrder(long amountInPaise, String receipt) throws RazorpayException {
        RazorpayClient client = new RazorpayClient(razorpayConfig.getKeyId(), razorpayConfig.getKeySecret());

        JSONObject request = new JSONObject();
        request.put("amount", amountInPaise);
        request.put("currency", razorpayConfig.getCurrency());
        request.put("receipt", receipt);

        Order order = client.orders.create(request);

        RazorpayOrderResponse response = new RazorpayOrderResponse();
        response.setId(order.get("id"));
        response.setEntity(order.get("entity"));
        response.setAmount(((Number) order.get("amount")).longValue());
        response.setAmountPaid(((Number) order.get("amount_paid")).longValue());
        response.setAmountDue(((Number) order.get("amount_due")).longValue());
        response.setCurrency(order.get("currency"));
        response.setStatus(order.get("status"));
        response.setReceipt(order.get("receipt"));
        response.setAttempts(((Number) order.get("attempts")).intValue());

        return response;
    }


    // VERIFY PAYMENT SIGNATURE
    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) throws RazorpayException{
        JSONObject attributes = new JSONObject();
        attributes.put("razorpay_order_id", orderId);
        attributes.put("razorpay_payment_id", paymentId);
        attributes.put("razorpay_signature", signature);

        return Utils.verifyPaymentSignature(attributes, razorpayConfig.getKeySecret());
    }

    public boolean verifyWebhookSignature(String payload, String signature) throws RazorpayException {
        return Utils.verifyWebhookSignature(payload, signature, razorpayConfig.getWebhookSecret());
    }


    public String getKeyId() {
        return razorpayConfig.getKeyId();
    }

}