package com.project.LinkedIn.post_service.service;

import com.project.LinkedIn.post_service.auth.UserContextHolder;
import com.project.LinkedIn.post_service.client.ConnectionClient;
import com.project.LinkedIn.post_service.dto.PersonDto;
import com.project.LinkedIn.post_service.dto.PostCreateRequestDto;
import com.project.LinkedIn.post_service.dto.PostDto;
import com.project.LinkedIn.post_service.entity.Post;
import com.project.LinkedIn.post_service.event.PostCreatedEvent;
import com.project.LinkedIn.post_service.exception.ResourceNotFoundException;
import com.project.LinkedIn.post_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ConnectionClient connectionClient;

    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

    public PostDto createPost(PostCreateRequestDto postCreateRequestDto) {
        Long userId = UserContextHolder.getCurrentUserId();
      Post post = modelMapper.map(postCreateRequestDto, Post.class);
      post.setUserId(userId);
      Post savedPost = postRepository.save(post);

      PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
              .postId(savedPost.getId())
              .creatorId(userId)
              .content(savedPost.getContent())
              .build();

      kafkaTemplate.send("postCreatedTopic", postCreatedEvent);

      return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.debug("Fetching post with ID: {}", postId);
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post not found with ID: " + postId)
        );
        return modelMapper.map(post, PostDto.class);
    }

    public List<PostDto> getAllPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);

        return posts.stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }
}
