package com.blog.blogapp.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class ArticleCategory extends TraceableEntity {

    @Column(nullable = false, unique = true)
    private String name;

}
