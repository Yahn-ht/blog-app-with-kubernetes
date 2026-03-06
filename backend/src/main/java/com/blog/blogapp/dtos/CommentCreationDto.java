package com.blog.blogapp.dtos;

import jakarta.validation.constraints.NotBlank;

public record CommentCreationDto(
        @NotBlank
        String content,
        Long articleId
) {

}
