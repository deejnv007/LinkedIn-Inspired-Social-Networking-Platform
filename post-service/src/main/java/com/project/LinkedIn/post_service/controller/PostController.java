package com.project.LinkedIn.post_service.controller;

import com.project.LinkedIn.post_service.auth.UserContextHolder;
import com.project.LinkedIn.post_service.dto.PostCreateRequestDto;
import com.project.LinkedIn.post_service.dto.PostDto;
import com.project.LinkedIn.post_service.entity.Post;
import com.project.LinkedIn.post_service.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

   private final PostService postService;

   @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postCreateRequestDto) {
       PostDto createdPostDto = postService.createPost(postCreateRequestDto);
       return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
   }

   @GetMapping("/{postId}")
   public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {
       PostDto postDto = postService.getPostById(postId);
       return ResponseEntity.ok(postDto);
   }

   @GetMapping("/users/{userId}/allposts")
   public ResponseEntity<List<PostDto>> getAllPostsByUserId(@PathVariable Long userId) {
      List<PostDto> posts = postService.getAllPostsByUserId(userId);
      return ResponseEntity.ok(posts);
   }
}
