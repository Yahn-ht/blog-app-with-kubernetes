package com.blog.blogapp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ArticleCreationDto(
        @NotBlank
        @Size(min= 5 , max = 255)
        String title,
        @Size(min= 5)
        String content,
        @Size(min= 5)
        String description,
        @NotNull
        LocalDateTime publishedAt,
        @NotNull
        Long categoryId
) {

}
