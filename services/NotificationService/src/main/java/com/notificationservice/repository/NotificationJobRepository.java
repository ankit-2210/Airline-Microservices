package com.notificationservice.repository;

import com.airlineportal.utils.Notification.NotificationStatus;
import com.notificationservice.model.NotificationJob;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface NotificationJobRepository extends JpaRepository<NotificationJob, Long> {
    boolean existsByEventKey(String eventKey);

    List<NotificationJob> findByStatusAndNextAttemptAtLessThanEqualOrderByCreatedAtAsc(NotificationStatus status, LocalDateTime now, Pageable pageable);

    @Modifying
    @Transactional
    @Query("""
        update NotificationJob n
           set n.status = :processing,
               n.updatedAt = :now
         where n.id = :id
           and n.status = :pending
           and n.nextAttemptAt <= :now
        """)
    int claim(@Param("id") Long id, @Param("pending") NotificationStatus pending, @Param("processing") NotificationStatus processing, @Param("now") LocalDateTime now);



}
