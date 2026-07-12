package com.project.LinkedIn.post_service.repository;

import com.project.LinkedIn.post_service.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    void deleteByuserIdAndPostId(long userId, Long postId);
}
