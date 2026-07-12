package com.project.LinkedIn.notification_service.client;

import com.project.LinkedIn.notification_service.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "connection-service", path="/connection")
public interface ConnectionClient {

    @GetMapping("/connections/first-degree")
    List<PersonDto> getFirstConnection(@RequestHeader("X-User-Id") Long  userId);
}
