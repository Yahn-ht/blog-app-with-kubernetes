package com.blog.blogapp.repositories;

import com.blog.blogapp.domains.ArticleCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory, Long> {
    boolean existsByName(String name);
}
