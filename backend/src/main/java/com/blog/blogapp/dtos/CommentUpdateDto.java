package com.blog.blogapp.dtos;

import jakarta.validation.constraints.NotBlank;

public record CommentUpdateDto(
        @NotBlank
        String content
) {

}
