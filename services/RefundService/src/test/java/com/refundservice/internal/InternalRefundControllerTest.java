package com.refundservice.internal;


import com.airlineportal.payload.response.Refund.RefundResponse;
import com.airlineportal.utils.Refund.RefundReason;
import com.airlineportal.utils.Refund.RefundStatus;

import com.refundservice.service.RefundService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class InternalRefundControllerTest {
    @Mock
    private RefundService refundService;

    private MockMvc mockMvc;

    private RefundResponse refundResponse;

    @BeforeEach
    void setUp() {
        InternalRefundController controller = new InternalRefundController(refundService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        refundResponse = RefundResponse.builder()
                .id(1L)
                .bookingId(100L)
                .userId(10L)
                .pnr("ABC123")
                .originalAmount(new BigDecimal("10000.00"))
                .cancellationFee(new BigDecimal("1000.00"))
                .refundAmount(new BigDecimal("9000.00"))
                .reason(RefundReason.BOOKING_CANCELLED)
                .status(RefundStatus.COMPLETED)
                .gatewayRefundId("rfnd_123")
                .build();
    }

    @Test
    void getById_shouldReturnSuccess() throws Exception {
        when(refundService.getById(1L))
                .thenReturn(refundResponse);

        mockMvc.perform(get("/internal/refunds/1"))
                .andExpect(status().isOk());

        verify(refundService).getById(1L);
    }

    @Test
    void getByBookingId_shouldReturnSuccess() throws Exception {
        when(refundService.getByBookingId(100L))
                .thenReturn(refundResponse);

        mockMvc.perform(get("/internal/refunds/booking/100"))
                .andExpect(status().isOk());

        verify(refundService).getByBookingId(100L);
    }

    @Test
    void getByStatus_shouldReturnSuccess() throws Exception {
        Page<RefundResponse> page = new PageImpl<>(List.of(refundResponse));

        when(refundService.getByStatus(
                eq("COMPLETED"), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/internal/refunds/status/COMPLETED")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(refundService).getByStatus(eq("COMPLETED"), any(Pageable.class));
    }




}
