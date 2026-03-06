package com.blog.blogapp.dtos;

import com.blog.blogapp.enumeration.Role;
import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        Role role
) {

}
