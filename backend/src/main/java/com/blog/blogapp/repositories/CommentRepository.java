package com.blog.blogapp.repositories;

import com.blog.blogapp.domains.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByUser_Id(Long userId,Pageable pageable);
    Page<Comment> findAllByArticle_Id(Long userId,Pageable pageable);
}
