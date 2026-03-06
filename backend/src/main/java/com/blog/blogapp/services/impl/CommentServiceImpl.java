package com.blog.blogapp.services.impl;

import com.blog.blogapp.domains.Article;
import com.blog.blogapp.domains.Comment;
import com.blog.blogapp.domains.User;
import com.blog.blogapp.dtos.CommentCreationDto;
import com.blog.blogapp.dtos.CommentDto;
import com.blog.blogapp.dtos.CommentUpdateDto;
import com.blog.blogapp.exceptions.ResourceNotFoundException;
import com.blog.blogapp.mappers.CommentMapper;
import com.blog.blogapp.repositories.ArticleRepository;
import com.blog.blogapp.repositories.CommentRepository;
import com.blog.blogapp.repositories.UserRepository;
import com.blog.blogapp.services.CommentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto commentAnArticle(String email,CommentCreationDto dto) {
        Objects.requireNonNull(email);
        Article article = articleRepository.findById(dto.articleId())
                .orElseThrow(()-> new ResourceNotFoundException("article.notfound"));
        User connectedUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("user.notfound"));

        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setUser(connectedUser);
        comment.setContent(dto.content());

        return commentMapper.toDtoForUser(commentRepository.save(comment));
    }

    @Override
    public CommentDto getCommentById(Long id) {
        Objects.requireNonNull(id);
        return commentMapper.toDto(commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("comment.notfound")));
    }

    @Override
    public CommentDto updateComment(Long id, CommentUpdateDto dto) {
        Objects.requireNonNull(id);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("comment.notfound"));

        checkOwnership(comment.getUser().getEmail());

        if(dto.content() != null && dto.content().equals(comment.getContent())){
            comment.setContent(dto.content());
        }

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public Page<CommentDto> getCommentsByUserId(Pageable pageable, Long userId) {
        Objects.requireNonNull(userId);
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("user.notfound"));

        return commentRepository.findAllByUser_Id(userId,pageable)
                .map(commentMapper::toDtoForUser);

    }

    @Override
    public Page<CommentDto> getCommentsByArticleId(Pageable pageable, Long articleId) {
        Objects.requireNonNull(articleId);
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("article.notfound"));

        return commentRepository.findAllByArticle_Id(articleId,pageable)
                .map(commentMapper::toDtoForArticle);
    }

    @Override
    public Page<CommentDto> findAllComments(Pageable pageable) {

        return commentRepository.findAll(pageable)
                .map(commentMapper::toDto);
    }

    @Override
    public void deleteComment(Long id) {
        Objects.requireNonNull(id);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("comment.notfound"));

        checkOwnership(comment.getUser().getEmail());

        commentRepository.delete(comment);
    }

    private void checkOwnership(String authorEmail) {
        String connectedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));

        if (!connectedEmail.equals(authorEmail) && !isAdmin) {
            throw new AccessDeniedException("Action interdite.");
        }
    }
}
