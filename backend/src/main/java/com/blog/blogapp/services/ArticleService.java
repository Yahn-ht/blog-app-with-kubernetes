package com.blog.blogapp.services;

import com.blog.blogapp.dtos.ArticleCreationDto;
import com.blog.blogapp.dtos.ArticleDto;
import com.blog.blogapp.dtos.UpdateArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {
    ArticleDto createArticle(String email, ArticleCreationDto dto);
    ArticleDto getArticleById(Long id);
    Page<ArticleDto> findAll(Pageable pageable, String searchKey, Long categoryId);
    Page<ArticleDto> findAllArticleByUserId(Long userId, Pageable pageable);
    Page<ArticleDto> findUserConnectedArticle(String email, Pageable pageable);
    ArticleDto updateArticle(Long id,UpdateArticleDto dto);
    void deleteArticle(Long id);
    void likeArticle(Long id,String email);
    void unlikeArticle(Long id,String email);
    ArticleDto getArticleBySlug(String slug);
    Page<ArticleDto> findByCategoryId(Long categoryId, Pageable pageable);
}
