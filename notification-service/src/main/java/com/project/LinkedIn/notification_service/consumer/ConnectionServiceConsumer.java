package com.project.LinkedIn.notification_service.consumer;

import com.project.LinkedIn.connection_service.event.AcceptConnectionRequestEvent;
import com.project.LinkedIn.connection_service.event.SendConnectionRequestEvent;
import com.project.LinkedIn.notification_service.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.internal.StandardEntityNotFoundDelegate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionServiceConsumer {

    private final SendNotification sendNotification;

    @KafkaListener(topics = "sendConnectionRequestTopic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent) {
        String message =
                "You have received a new connection request from user with userId: %d" + sendConnectionRequestEvent.getSenderId();

        sendNotification.notify(sendConnectionRequestEvent.getReceiverId(), message);
        log.info("Received SendConnectionRequest Notification {}", sendConnectionRequestEvent);
    }

    @KafkaListener(topics = "acceptConnectionRequestTopic")
    public void handleAcceptConnectionRequest(AcceptConnectionRequestEvent acceptConnectionRequestEvent) {
        String message =
                "Your connection request has been accept by user with userId: %d" + acceptConnectionRequestEvent.getReceiverId();

        sendNotification.notify(acceptConnectionRequestEvent.getSenderId(), message);
        log.info("Received AcceptConnectionRequest Notification {}", acceptConnectionRequestEvent);
    }
}
