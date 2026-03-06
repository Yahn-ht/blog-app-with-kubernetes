package com.blog.blogapp.domains;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "article_id"})
)

public class ArticleLike extends TraceableEntity{

    @Column(nullable = true)
    private String emoji;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "article_id")
    private Article article;
}
