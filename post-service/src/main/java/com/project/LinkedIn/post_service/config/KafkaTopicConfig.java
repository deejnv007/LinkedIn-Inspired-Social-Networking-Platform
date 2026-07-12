package com.project.LinkedIn.post_service.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class KafkaTopicConfig {

    @Bean
    public NewTopic  postCreatedTopic() {
        log.info("Creating postCreatedTopic");
        return new NewTopic("postCreatedTopic", 3, (short) 1);
    }

    @Bean
    public NewTopic postLikedTopic() {
        log.info("Creating postLikedTopic");
        return new NewTopic("postLikedTopic", 3, (short) 1);
    }
}
