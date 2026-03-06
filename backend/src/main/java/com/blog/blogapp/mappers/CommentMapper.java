package com.blog.blogapp.mappers;

import com.blog.blogapp.domains.Comment;
import com.blog.blogapp.dtos.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class}
)
public interface CommentMapper {

    CommentDto toDto(Comment comment);

    @Mapping(target = "user" , ignore = true)
    CommentDto toDtoForUser(Comment comment);

    @Mapping(target = "article", ignore = true)
    CommentDto toDtoForArticle(Comment comment);

    Comment toEntity(CommentDto dto);

    List<Comment> toEntities(List<CommentDto> dtos);

    default List<CommentDto> toDtosForUser(List<Comment> comments) {
        return comments.stream()
                .map(this::toDtoForUser)
                .toList();
    }

    default List<CommentDto> toDtosForArticle(List<Comment> comments) {
        return comments.stream()
                .map(this::toDtoForArticle)
                .toList();
    }

    default List<CommentDto> toDtos(List<Comment> comments) {
        return comments.stream()
                .map(this::toDto)
                .toList();
    }
}
