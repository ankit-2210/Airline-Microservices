package com.paymentservice.controller;


import com.paymentservice.model.Payment;
import com.paymentservice.service.PaymentCallbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments/callback")
public class PaymentCallbackController {
    private final PaymentCallbackService paymentCallbackService;

    @GetMapping(value = "/razorpay", produces = MediaType.TEXT_HTML_VALUE)
    public String handleRazorpayCallback(
            @RequestParam("razorpay_payment_id") String paymentId,
            @RequestParam("razorpay_payment_link_id") String paymentLinkId,
            @RequestParam("razorpay_payment_link_reference_id") String referenceId,
            @RequestParam("razorpay_payment_link_status") String status,
            @RequestParam("razorpay_signature") String signature) {

        Payment payment = paymentCallbackService.handlePaymentLinkCallback(paymentId, paymentLinkId, referenceId, status, signature);

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Airline Payment</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background: #f5f7fa;
                            text-align: center;
                            padding-top: 80px;
                        }

                        .box {
                            width: 500px;
                            margin: auto;
                            background: white;
                            padding: 40px;
                            border-radius: 12px;
                            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
                        }

                        h1 {
                            color: green;
                        }

                        p {
                            font-size: 17px;
                        }
                    </style>
                </head>

                <body>

                    <div class="box">
                        <h1>Payment Successful</h1>
                        <p>
                            Your airline booking payment
                            was completed successfully.
                        </p>
                        <p>
                            <b>Booking ID:</b> %s
                        </p>
                        <p>
                            <b>PNR:</b> %s
                        </p>
                        <p>
                            <b>Payment ID:</b> %s
                        </p>
                        <p>
                            <b>Amount:</b> ₹%s
                        </p>
                        <p>
                            <b>Status:</b> SUCCESS
                        </p>

                    </div>

                </body>
                </html>
                """.formatted(
                payment.getBookingId(),
                payment.getPnr(),
                payment.getRazorpayPaymentId(),
                payment.getAmount()
        );
    }

}
