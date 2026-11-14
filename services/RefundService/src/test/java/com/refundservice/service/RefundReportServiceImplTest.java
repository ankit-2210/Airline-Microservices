package com.refundservice.service;


import com.airlineportal.utils.Refund.RefundReason;
import com.airlineportal.utils.Refund.RefundStatus;
import com.refundservice.model.Refund;
import com.refundservice.repository.RefundRepository;
import com.refundservice.service.Impl.RefundReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
public class RefundReportServiceImplTest {
    @Mock
    private RefundRepository refundRepository;

    @InjectMocks
    private RefundReportServiceImpl refundReportService;

    private Refund refund;

    @BeforeEach
    void setUp(){
        refund = Refund.builder()
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
                .remarks("Refund completed")
                .build();
    }

    @Test
    void generateRefundsPdf_shouldGeneratePdfSuccessfully() {
        doReturn(List.of(refund))
                .when(refundRepository)
                .findAll();

        byte[] pdf = refundReportService.generateRefundsPdf();

        assertThat(pdf).isNotNull();
        assertThat(pdf).isNotEmpty();
        assertThat(new String(pdf, 0, 4))
                .isEqualTo("%PDF");

        verify(refundRepository).findAll();
    }

    @Test
    void generateRefundsPdf_shouldThrowExceptionWhenRepositoryFails() {
        RuntimeException databaseException = new RuntimeException("Database unavailable");

        doThrow(databaseException)
                .when(refundRepository)
                .findAll();

        assertThatThrownBy(() ->
                refundReportService.generateRefundsPdf())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to load refund PDF report")
                .hasCause(databaseException);

        verify(refundRepository).findAll();
    }


}
