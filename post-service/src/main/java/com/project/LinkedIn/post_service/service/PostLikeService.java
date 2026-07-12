package com.project.LinkedIn.post_service.service;

import com.project.LinkedIn.post_service.auth.UserContextHolder;
import com.project.LinkedIn.post_service.entity.Post;
import com.project.LinkedIn.post_service.entity.PostLike;
import com.project.LinkedIn.post_service.event.PostLikedEvent;
import com.project.LinkedIn.post_service.exception.BadRequestException;
import com.project.LinkedIn.post_service.exception.ResourceNotFoundException;
import com.project.LinkedIn.post_service.repository.PostLikeRepository;
import com.project.LinkedIn.post_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository  postRepository;
    private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

    public void likePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("attempting to like the post with id: {}", postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post not found with Id: "+postId));
//        boolean exists = postRepository.existsById(postId);
//        if(!exists) throw new ResourceNotFoundException("Post with id " + postId + " not found");

        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(alreadyLiked) throw new BadRequestException("Can't like the same post again.");

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);

        log.info("liked the post with id: {}", postId);

        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                .postId(postId)
                .likedUserId(userId)
                .creatorId(post.getUserId())
                .build();

        kafkaTemplate.send("postLikedTopic", postId, postLikedEvent);
    }

    @Transactional
    public void unlikePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("attempting to unlike the post with id: {}", postId);
        boolean exists = postRepository.existsById(postId);
        if(!exists) throw new ResourceNotFoundException("Post with id " + postId + " not found");

        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(!alreadyLiked) throw new BadRequestException("Can't unlike the post which is not liked.");

        postLikeRepository.deleteByuserIdAndPostId(userId, postId);

        log.info("unliked the post with id: {}", postId);
    }
}
