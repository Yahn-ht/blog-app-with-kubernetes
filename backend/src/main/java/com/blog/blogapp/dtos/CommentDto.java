package com.blog.blogapp.dtos;

import lombok.Data;

public record CommentDto(
         Long id,
         String content,
         UserDto user,
         ArticleDto article
) {

}
