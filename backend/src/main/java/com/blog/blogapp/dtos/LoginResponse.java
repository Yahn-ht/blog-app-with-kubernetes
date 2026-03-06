package com.blog.blogapp.dtos;

import lombok.Builder;

@Builder
public record LoginResponse(
        String token
) {

}
