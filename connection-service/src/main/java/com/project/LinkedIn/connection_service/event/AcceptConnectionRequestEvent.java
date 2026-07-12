package com.project.LinkedIn.connection_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcceptConnectionRequestEvent {
    Long senderId;
    Long receiverId;
}
