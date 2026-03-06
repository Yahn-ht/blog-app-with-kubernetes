package com.blog.blogapp.services;

import com.blog.blogapp.dtos.CommentCreationDto;
import com.blog.blogapp.dtos.CommentDto;
import com.blog.blogapp.dtos.CommentUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    CommentDto commentAnArticle(String email,CommentCreationDto dto);
    CommentDto getCommentById(Long id);
    CommentDto updateComment(Long id, CommentUpdateDto dto);
    Page<CommentDto> getCommentsByUserId(Pageable pageable, Long userId);
    Page<CommentDto> getCommentsByArticleId(Pageable pageable,Long articleId);
    Page<CommentDto> findAllComments(Pageable pageable);
    void deleteComment(Long id);
}
