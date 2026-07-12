package com.project.LinkedIn.post_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLikedEvent {
    Long postId;
    Long creatorId;
    Long likedUserId;
}
