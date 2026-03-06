package com.blog.blogapp.repositories;

import com.blog.blogapp.domains.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    Optional<ArticleLike> findByUser_IdAndArticle_Id(Long userId, Long articleId);
    boolean existsByArticle_IdAndUser_Id(Long userId, Long articleId);
}
