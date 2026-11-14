package com.refundservice.controller;


import com.airlineportal.payload.request.Refund.RefundCreateRequest;
import com.airlineportal.payload.request.Refund.RefundStatusUpdateRequest;
import com.airlineportal.payload.response.Refund.RefundResponse;
import com.airlineportal.utils.Refund.RefundReason;
import com.airlineportal.utils.Refund.RefundStatus;

import com.refundservice.service.RefundService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class RefundControllerTest {
    @Mock
    private RefundService refundService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private RefundResponse refundResponse;

    @BeforeEach
    void setUp() {
        RefundController refundController = new RefundController(refundService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(refundController)
                .build();

        objectMapper = new ObjectMapper()
                .findAndRegisterModules();

        refundResponse = RefundResponse.builder()
                .id(1L)
                .bookingId(100L)
                .userId(10L)
                .pnr("ABC123")
                .originalAmount(new BigDecimal("10000.00"))
                .cancellationFee(new BigDecimal("1000.00"))
                .refundAmount(new BigDecimal("9000.00"))
                .reason(RefundReason.BOOKING_CANCELLED)
                .status(RefundStatus.REQUESTED)
                .remarks("Customer cancelled")
                .build();
    }


    @Test
    void createRefund_shouldReturnSuccess() throws Exception {
        RefundCreateRequest request = RefundCreateRequest.builder()
                        .bookingId(100L)
                        .reason(RefundReason.BOOKING_CANCELLED)
                        .remarks("Customer cancelled")
                        .build();

        when(refundService.createRefund(any()))
                .thenReturn(refundResponse);

        mockMvc.perform(post("/api/refunds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(refundService).createRefund(any());

    }

    @Test
    void getById_shouldReturnSuccess() throws Exception {
        when(refundService.getById(1L))
                .thenReturn(refundResponse);

        mockMvc.perform(get("/api/refunds/1"))
                .andExpect(status().isOk());

        verify(refundService).getById(1L);
    }

    @Test
    void getByBookingId_shouldReturnSuccess() throws Exception {
        when(refundService.getByBookingId(100L))
                .thenReturn(refundResponse);

        mockMvc.perform(get("/api/refunds/booking/100"))
                .andExpect(status().isOk());

        verify(refundService).getByBookingId(100L);
    }





}
