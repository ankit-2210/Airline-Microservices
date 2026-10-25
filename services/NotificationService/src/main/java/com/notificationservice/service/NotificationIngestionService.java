package com.notificationservice.service;

public interface NotificationIngestionService {

    void ingest(String eventKey, String eventType, Long userId, String subject, String body);


}
