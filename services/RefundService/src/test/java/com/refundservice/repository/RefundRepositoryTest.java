package com.refundservice.repository;

import com.airlineportal.utils.Refund.RefundReason;
import com.airlineportal.utils.Refund.RefundStatus;
import com.refundservice.model.Refund;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.sql.Ref;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RefundRepositoryTest {
    @Autowired
    private RefundRepository refundRepository;

    private Refund refund1;
    private Refund refund2;

    @BeforeEach
    void setUp() {

        refundRepository.deleteAll();

        refund1 = Refund.builder()
                .bookingId(100L)
                .userId(10L)
                .paymentId(500L)
                .pnr("ABC123")
                .originalAmount(new BigDecimal("10000.00"))
                .cancellationFee(new BigDecimal("1000.00"))
                .refundAmount(new BigDecimal("9000.00"))
                .reason(RefundReason.BOOKING_CANCELLED)
                .status(RefundStatus.REQUESTED)
                .remarks("Customer cancelled")
                .build();

        refund2 = Refund.builder()
                .bookingId(200L)
                .userId(10L)
                .paymentId(600L)
                .pnr("XYZ456")
                .originalAmount(new BigDecimal("5000.00"))
                .cancellationFee(new BigDecimal("500.00"))
                .refundAmount(new BigDecimal("4500.00"))
                .reason(RefundReason.BOOKING_CANCELLED)
                .status(RefundStatus.COMPLETED)
                .gatewayRefundId("rfnd_123")
                .remarks("Refund completed")
                .build();

        refundRepository.save(refund1);
        refundRepository.save(refund2);
    }

    // findByBookingId()
    @Test
    void findByBookingId_shouldReturnRefund() {
        Optional<Refund> result = refundRepository.findByBookingId(100L);

        assertThat(result).isPresent();

        Refund refund = result.get();
        assertThat(refund.getBookingId())
                .isEqualTo(100L);

        assertThat(refund.getPnr())
                .isEqualTo("ABC123");

        assertThat(refund.getRefundAmount())
                .isEqualByComparingTo("9000.00");
    }

    @Test
    void findByBookingId_shouldReturnEmptyWhenNotFound() {
        Optional<Refund> result = refundRepository.findByBookingId(999L);

        assertThat(result).isEmpty();
    }

    // findAllByBookingId()
    @Test
    void findAllByBookingId_shouldReturnMatchingRefunds() {
        List<Refund> result = refundRepository.findAllByBookingId(100L);

        assertThat(result)
                .hasSize(1);

        assertThat(result.get(0).getBookingId())
                .isEqualTo(100L);
    }

    @Test
    void findAllByBookingId_shouldReturnEmptyListWhenNotFound() {
        List<Refund> result = refundRepository.findAllByBookingId(999L);

        assertThat(result).isEmpty();
    }

    // findByBookingId(pageable)
    @Test
    void findByBookingIdWithPageable_shouldReturnPage() {

        Page<Refund> result = refundRepository.findByBookingId(
                        100L,
                        PageRequest.of(0, 10));

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements())
                .isEqualTo(1);

        assertThat(result.getContent())
                .hasSize(1);

        assertThat(result.getContent().get(0).getBookingId())
                .isEqualTo(100L);
    }

    // findByUserId()
    @Test
    void findByUserId_shouldReturnRefundsForUser() {

        Page<Refund> result =
                refundRepository.findByUserId(
                        10L,
                        PageRequest.of(0, 10)
                );

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements())
                .isEqualTo(2);
        assertThat(result.getContent())
                .hasSize(2);
        assertThat(result.getContent())
                .allMatch(refund ->
                        refund.getUserId().equals(10L));

    }

    @Test
    void findByUserId_shouldReturnEmptyPageWhenUserNotFound() {
        Page<Refund> result =
                refundRepository.findByUserId(
                        999L,
                        PageRequest.of(0, 10)
                );

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements())
                .isZero();
        assertThat(result.getContent())
                .isEmpty();
    }

    // findByStatus()




    // existsByBookingId()
    @Test
    void existsByBookingId_shouldReturnTrueWhenRefundExists() {
        boolean exists = refundRepository.existsByBookingId(100L);

        assertThat(exists).isTrue();
    }

    @Test
    void existsByBookingId_shouldReturnFalseWhenRefundDoesNotExist() {
        boolean exists = refundRepository.existsByBookingId(999L);

        assertThat(exists).isFalse();
    }

    // save()
    @Test
    void save_shouldPersistRefund() {
        Refund refund = Refund.builder()
                .bookingId(300L)
                .userId(20L)
                .paymentId(700L)
                .pnr("NEW123")
                .originalAmount(new BigDecimal("2000.00"))
                .cancellationFee(new BigDecimal("200.00"))
                .refundAmount(new BigDecimal("1800.00"))
                .reason(RefundReason.BOOKING_CANCELLED)
                .status(RefundStatus.REQUESTED)
                .remarks("New refund")
                .build();

        Refund saved = refundRepository.save(refund);

        assertThat(saved.getId())
                .isNotNull();
        assertThat(saved.getBookingId())
                .isEqualTo(300L);
        assertThat(saved.getPnr())
                .isEqualTo("NEW123");
        assertThat(saved.getStatus())
                .isEqualTo(RefundStatus.REQUESTED);
    }

    // delete()
    @Test
    void delete_shouldRemoveRefund() {
        Long refundId = refund1.getId();

        refundRepository.delete(refund1);
        Optional<Refund> result = refundRepository.findById(refundId);

        assertThat(result).isEmpty();

    }



}
