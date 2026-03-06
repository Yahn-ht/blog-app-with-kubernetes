package com.blog.blogapp.mappers;

import com.blog.blogapp.domains.ArticleCategory;
import com.blog.blogapp.dtos.ArticleCategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ArticleCategoryMapper {
    ArticleCategory toEntity(ArticleCategoryDto dto);
    ArticleCategoryDto toDto(ArticleCategory articleCategory);

    List<ArticleCategory> toEntities(List<ArticleCategoryDto> dtos);
    List<ArticleCategoryDto> toDtos(List<ArticleCategory> articleCategories);
}
