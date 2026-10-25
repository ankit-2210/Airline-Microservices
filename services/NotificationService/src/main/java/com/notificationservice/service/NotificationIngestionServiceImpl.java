package com.notificationservice.service;


import com.notificationservice.repository.NotificationJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationIngestionServiceImpl implements NotificationIngestionService{
    private final NotificationJobRepository notificationJobRepository;

    @Override
    public void ingest(String eventKey, String eventType, Long userId, String subject, String body) {

    }
}
