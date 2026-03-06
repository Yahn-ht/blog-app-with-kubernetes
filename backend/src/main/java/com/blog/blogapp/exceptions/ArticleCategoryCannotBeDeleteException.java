package com.blog.blogapp.exceptions;

public class ArticleCategoryCannotBeDeleteException extends RuntimeException {
    public ArticleCategoryCannotBeDeleteException(String message) {
        super(message);
    }
}
