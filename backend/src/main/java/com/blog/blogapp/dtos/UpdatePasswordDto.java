package com.blog.blogapp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdatePasswordDto(
        @NotBlank
        String oldPassword,
        @Size(min = 5)
        @NotBlank
        String newPassword
) {

}
