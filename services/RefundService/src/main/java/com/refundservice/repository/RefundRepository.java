package com.refundservice.repository;

import com.airlineportal.utils.Refund.RefundStatus;
import com.refundservice.model.Refund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util. *;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {
    Optional<Refund> findByBookingId(Long bookingId);

    List<Refund> findAllByBookingId(Long bookingId);

    Page<Refund> findByBookingId(Long bookingId, Pageable pageable);
    Page<Refund> findByUserId(Long userId, Pageable pageable);
    Page<Refund> findByStatus(RefundStatus status, Pageable pageable);

    boolean existsByBookingId(Long bookingId);



}
