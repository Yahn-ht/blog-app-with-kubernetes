package com.blog.blogapp.services;

import com.blog.blogapp.dtos.ArticleCategoryDto;
import com.blog.blogapp.dtos.CreateOrUpdateArticleCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleCategoryService {
    ArticleCategoryDto createCategory(CreateOrUpdateArticleCategoryDto dto);
    ArticleCategoryDto updateCategory(Long id,CreateOrUpdateArticleCategoryDto dto);
    List<ArticleCategoryDto> getAllCategories ();
    ArticleCategoryDto getCategoryById(Long id);
    void deleteCategory(Long id);
}
