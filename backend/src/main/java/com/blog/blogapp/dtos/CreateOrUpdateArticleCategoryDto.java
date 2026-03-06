package com.blog.blogapp.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateOrUpdateArticleCategoryDto(
        @NotBlank
        String name
) {

}
