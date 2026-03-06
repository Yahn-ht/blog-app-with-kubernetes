package com.blog.blogapp.dtos;

import lombok.Builder;

@Builder
public record UpdateProfileDto(
        String firstName,
        String lastName
) {

}
