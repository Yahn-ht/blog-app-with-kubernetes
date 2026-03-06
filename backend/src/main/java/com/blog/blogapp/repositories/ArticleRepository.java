package com.blog.blogapp.repositories;

import aj.org.objectweb.asm.commons.Remapper;
import com.blog.blogapp.domains.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAllByAuthor_Id(Long authorId, Pageable pageable);

    @Modifying
    @Query("""
        UPDATE Article a
        SET a.likeCount = a.likeCount + 1
        WHERE a.id = :articleId
    """)
    void incrementLikeCount(Long articleId);

    @Modifying
    @Query("""
        UPDATE Article a
        SET a.likeCount = a.likeCount - 1
        WHERE a.id = :articleId
    """)
    void decrementLikeCount(Long articleId);

    boolean existsBySlug(String slug);

    Optional<Article> findBySlug(String slug);

    boolean existsByCategory_Id(Long id);

    Page<Article> findByCategory_Id(Long categoryId, Pageable pageable);
}
