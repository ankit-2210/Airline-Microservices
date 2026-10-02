package com.paymentservice.external;

import com.paymentservice.config.RazorpayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RazorpayService {
    private final RazorpayConfig razorpayConfig;



}
