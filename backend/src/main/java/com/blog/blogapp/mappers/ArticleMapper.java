package com.blog.blogapp.mappers;

import com.blog.blogapp.domains.Article;
import com.blog.blogapp.dtos.ArticleCreationDto;
import com.blog.blogapp.dtos.ArticleDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class, ArticleCategoryMapper.class}
)
public interface ArticleMapper {
    ArticleDto toDto(Article article);
    Article toEntity(ArticleDto dto);

    Article fromArticleCreationDto(ArticleCreationDto dto);

    @Mapping(target = "author", ignore = true)
    ArticleDto toUserArticleDto(Article article);
}
