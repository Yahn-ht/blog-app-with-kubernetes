package com.blog.blogapp.controllers;

import com.blog.blogapp.consts.Consts;
import com.blog.blogapp.dtos.CommentCreationDto;
import com.blog.blogapp.dtos.CommentDto;
import com.blog.blogapp.dtos.CommentUpdateDto;
import com.blog.blogapp.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Consts.API_BASE + Consts.API_VERSION + "comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> commentArticle(Authentication authentication, @Valid @RequestBody CommentCreationDto dto){
        String email = authentication.getName();
        CommentDto commentDto = commentService.commentAnArticle(email,dto);

        return ResponseEntity.created(URI.create(Consts.API_BASE + Consts.API_VERSION + "comments/" + commentDto.id()))
                .body(commentDto);
    }

    @GetMapping("/{commentId}")
    ResponseEntity<CommentDto> getCommentById(@PathVariable Long commentId){
        return  new ResponseEntity<>(commentService.getCommentById(commentId), HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    ResponseEntity<CommentDto> updateComment(@PathVariable Long commentId, @Valid @RequestBody CommentUpdateDto dto){
        return  new ResponseEntity<>(commentService.updateComment(commentId,dto), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<Page<CommentDto>> getCommentsByUserId(@PageableDefault Pageable pageable, @PathVariable Long userId){
        return  new ResponseEntity<>(commentService.getCommentsByUserId(pageable,userId), HttpStatus.OK);
    }

    @GetMapping("/article/{articleId}")
    ResponseEntity<Page<CommentDto>> getCommentsByArticleId(@PageableDefault Pageable pageable, @PathVariable Long articleId){
        return  new ResponseEntity<>(commentService.getCommentsByArticleId(pageable,articleId), HttpStatus.OK);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<Page<CommentDto>> findAllComments(@PageableDefault Pageable pageable){
        return  new ResponseEntity<>(commentService.findAllComments(pageable), HttpStatus.OK);
    }

    @DeleteMapping("{commentId}")
    ResponseEntity<Void> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
