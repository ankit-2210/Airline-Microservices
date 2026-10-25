package com.notificationservice.model;

import com.airlineportal.utils.Notification.NotificationChannel;
import com.airlineportal.utils.Notification.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "notification_jobs",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_notification_event_channel", columnNames = {"event_key", "channel"})
        },
        indexes = {
                @Index(name = "idx_notification_due", columnList = "status, next_attempt_at"),
                @Index(name = "idx_notification_user_created", columnList = "user_id, created_at")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_key", nullable = false, length = 150)
    private String eventKey;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "recipient_email", length = 320)
    private String recipientEmail;

    @Column(name = "subject", nullable = false, length = 255)
    private String subject;

    @Column(name = "template_name", nullable = false, length = 150)
    private String templateName;

    @Column(name = "payload_json", nullable = false, columnDefinition = "TEXT")
    private String payloadJson;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 30)
    private NotificationChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private NotificationStatus status;

    @Column(name = "attempt_count", nullable = false)
    @Builder.Default
    private Integer attemptCount = 0;

    @Column(name = "next_attempt_at")
    private Instant nextAttemptAt;

    @Column(name = "locked_at")
    private Instant lockedAt;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    @Builder.Default
    private Long version = 0L;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();

        if (createdAt == null) {
            createdAt = now;
        }

        updatedAt = now;

        if (status == null) {
            status = NotificationStatus.PENDING;
        }

        if (channel == null) {
            channel = NotificationChannel.EMAIL;
        }

        if (attemptCount == null) {
            attemptCount = 0;
        }

        if (nextAttemptAt == null) {
            nextAttemptAt = now;
        }

        if (version == null) {
            version = 0L;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public void markProcessing() {
        this.status = NotificationStatus.PROCESSING;
        this.lockedAt = Instant.now();
        this.attemptCount = this.attemptCount + 1;
        this.lastError = null;
    }

    public void markSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = Instant.now();
        this.lockedAt = null;
        this.nextAttemptAt = null;
        this.lastError = null;
    }

    public void markFailed(String error, Instant retryAt) {
        this.lastError = error;
        this.lockedAt = null;

        if (retryAt == null) {
            this.status = NotificationStatus.FAILED;
            this.nextAttemptAt = null;
        }
        else {
            this.status = NotificationStatus.PENDING;
            this.nextAttemptAt = retryAt;
        }
    }


}
















