package com.project.LinkedIn.post_service.client;

import com.project.LinkedIn.post_service.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "connection-service", path="/connection", url = "${CONNECTION_SERVICE_URI:}")
public interface ConnectionClient {

    @GetMapping("/connections/first-degree")
    List<PersonDto> getFirstConnection();
}
