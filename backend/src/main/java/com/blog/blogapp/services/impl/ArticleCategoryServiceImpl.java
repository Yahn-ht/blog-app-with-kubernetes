package com.blog.blogapp.services.impl;

import com.blog.blogapp.domains.ArticleCategory;
import com.blog.blogapp.dtos.ArticleCategoryDto;
import com.blog.blogapp.dtos.CreateOrUpdateArticleCategoryDto;
import com.blog.blogapp.exceptions.ArticleCategoryCannotBeDeleteException;
import com.blog.blogapp.exceptions.ResourceAlreadyExistsException;
import com.blog.blogapp.exceptions.ResourceNotFoundException;
import com.blog.blogapp.mappers.ArticleCategoryMapper;
import com.blog.blogapp.repositories.ArticleCategoryRepository;
import com.blog.blogapp.repositories.ArticleRepository;
import com.blog.blogapp.services.ArticleCategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleCategoryServiceImpl implements ArticleCategoryService {

    private final ArticleCategoryRepository articleCategoryRepository;
    private final ArticleCategoryMapper articleCategoryMapper;
    private final ArticleRepository articleRepository;

    @Override
    public ArticleCategoryDto createCategory(CreateOrUpdateArticleCategoryDto dto) {
        if(dto.name() != null && articleCategoryRepository.existsByName(dto.name())){
            throw new ResourceAlreadyExistsException("articleCategory.exists");
        }
        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setName(dto.name());
        return articleCategoryMapper.toDto(articleCategoryRepository.save(articleCategory));
    }

    @Override
    public ArticleCategoryDto updateCategory(Long id, CreateOrUpdateArticleCategoryDto dto) {
        Objects.requireNonNull(id);
        ArticleCategory articleCategory = articleCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("articleCategory.notfound"));
        if(dto.name() != null && !dto.name().equals(articleCategory.getName())){
            articleCategory.setName(dto.name());
        }
        return articleCategoryMapper.toDto(articleCategoryRepository.save(articleCategory));
    }

    @Override
    public List<ArticleCategoryDto> getAllCategories() {
        return articleCategoryRepository.findAll()
                .stream()
                .map(articleCategoryMapper::toDto)
                .toList();
    }

    @Override
    public ArticleCategoryDto getCategoryById(Long id) {
        Objects.requireNonNull(id);
        ArticleCategory articleCategory = articleCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("articleCategory.notfound"));
        return articleCategoryMapper.toDto(articleCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Objects.requireNonNull(id);
        ArticleCategory articleCategory = articleCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("articleCategory.notfound"));

        if(articleRepository.existsByCategory_Id(articleCategory.getId())){
            throw new ArticleCategoryCannotBeDeleteException("articleCategory.notdelete");
        }

        articleCategoryRepository.delete(articleCategory);
    }
}
