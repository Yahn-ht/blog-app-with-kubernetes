package com.blog.blogapp.dtos;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ArticleDto(
         Long id,
         String title,
         String content,
         String description,
         LocalDateTime publishedAt,
         Integer likeCount,
         String slug,
         UserDto author,
         ArticleCategoryDto category
) {

}
