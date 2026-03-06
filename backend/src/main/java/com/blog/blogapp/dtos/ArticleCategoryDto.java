package com.blog.blogapp.dtos;

import lombok.Builder;

@Builder
public record ArticleCategoryDto(
        Long id,
        String name
) {

}
