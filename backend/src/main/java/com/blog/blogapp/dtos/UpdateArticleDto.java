package com.blog.blogapp.dtos;

import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UpdateArticleDto(
        @Size(min= 5 , max = 255)
        String title,
        @Size(min= 5)
        String content,
        @Size(min= 5)
        String description,
        Long categoryId,
        LocalDateTime publishedAt
) {

}
