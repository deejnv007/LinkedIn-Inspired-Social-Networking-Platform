package com.project.LinkedIn.notification_service.consumer;

import com.project.LinkedIn.notification_service.client.ConnectionClient;
import com.project.LinkedIn.notification_service.dto.PersonDto;
import com.project.LinkedIn.notification_service.entity.Notification;
import com.project.LinkedIn.notification_service.repository.NotificationRepository;
import com.project.LinkedIn.notification_service.service.SendNotification;
import com.project.LinkedIn.post_service.event.PostCreatedEvent;
import com.project.LinkedIn.post_service.event.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceConsumer {

    private final ConnectionClient connectionClient;
    private final SendNotification sendNotification;

    @KafkaListener(topics = "postCreatedTopic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent) {
        log.info("Sending notification-handlePostCreated: {}", postCreatedEvent);
        List<PersonDto> connections = connectionClient.getFirstConnection(postCreatedEvent.getCreatorId());

        for (PersonDto connection : connections) {
           sendNotification.notify(connection.getUserId(), "Your Connection " + postCreatedEvent.getCreatorId() + " has created a post. Check that out.");
        }
    }

    @KafkaListener(topics = "postLikedTopic")
    public  void handlePostLiked(PostLikedEvent postLikedEvent) {
        log.info("Sending notification-handlePostLiked:  {}", postLikedEvent);

        String message = String.format("Your post, %d has been liked by %d",  postLikedEvent.getPostId(), postLikedEvent.getLikedUserId());

        sendNotification.notify(postLikedEvent.getCreatorId(), message);

    }

}
