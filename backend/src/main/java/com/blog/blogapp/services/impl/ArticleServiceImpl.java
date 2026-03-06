package com.blog.blogapp.services.impl;

import com.blog.blogapp.domains.*;
import com.blog.blogapp.dtos.ArticleCreationDto;
import com.blog.blogapp.dtos.ArticleDto;
import com.blog.blogapp.dtos.UpdateArticleDto;
import com.blog.blogapp.exceptions.ResourceAlreadyExistsException;
import com.blog.blogapp.exceptions.ResourceNotFoundException;
import com.blog.blogapp.repositories.ArticleCategoryRepository;
import com.blog.blogapp.mappers.ArticleMapper;
import com.blog.blogapp.repositories.ArticleLikeRepository;
import com.blog.blogapp.repositories.ArticleRepository;
import com.blog.blogapp.repositories.UserRepository;
import com.blog.blogapp.services.ArticleService;
import com.blog.blogapp.utils.SlugUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleCategoryRepository articleCategoryRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public ArticleDto createArticle(String email, ArticleCreationDto dto) {
        Objects.requireNonNull(email);
        Article article = articleMapper.fromArticleCreationDto(dto);

        ArticleCategory articleCategory = articleCategoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("articleCategory.notfound"));
        User author = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("user.notfound"));
        article.setAuthor(author);
        article.setCategory(articleCategory);
        article.setSlug(generateUniqueSlug(article.getTitle()));
        return articleMapper.toDto(articleRepository.save(article));
    }

    @Override
    public ArticleDto getArticleById(Long id) {
        Objects.requireNonNull(id);
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("article.notfound"));
        return articleMapper.toDto(article);
    }

    @Override
    public Page<ArticleDto> findAll(Pageable pageable, String searchKey, Long categoryId) {
        QUser user = QUser.user;
        QArticle article = QArticle.article;
        QArticleCategory category = QArticleCategory.articleCategory;

        BooleanBuilder predicate = new BooleanBuilder();

        if (searchKey != null && !searchKey.isEmpty()) {
            predicate.and(
                    article.title.containsIgnoreCase(searchKey)
                            .or(article.description.containsIgnoreCase(searchKey))
                            .or(article.content.containsIgnoreCase(searchKey))
            );
        }

        if (categoryId != null && articleCategoryRepository.existsById(categoryId)) {
            predicate.and(article.category.id.eq(categoryId));
        }

        List<Article> articles = queryFactory.selectFrom(article)
                .join(article.author,user)
                .join(article.category,category)
                .where(predicate)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(article.publishedAt.desc())
                .fetch();

        Long total = queryFactory
                .select(article.count())
                .from(article)
                .where(predicate)
                .fetchOne();

        return new PageImpl<>(
                articles.stream().map(articleMapper::toDto).toList(),
                pageable,
                total != null ? total : 0
        );
    }

    @Override
    public Page<ArticleDto> findAllArticleByUserId(Long userId, Pageable pageable) {
        Objects.requireNonNull(userId);
        return articleRepository.findAllByAuthor_Id(userId,pageable)
                .map(articleMapper::toDto);
    }

    @Override
    public Page<ArticleDto> findUserConnectedArticle(String email, Pageable pageable) {
        Objects.requireNonNull(email);
        User connectedUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("user.notfound"));
        return articleRepository.findAllByAuthor_Id(connectedUser.getId(), pageable)
                .map(articleMapper::toUserArticleDto);
    }

    @Override
    public ArticleDto updateArticle(Long id,UpdateArticleDto dto) {
        Objects.requireNonNull(id);

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("article.notfound"));

        checkOwnership(article.getAuthor().getEmail());

        if(dto.title() != null){
            article.setTitle(dto.title());
        }
        if(dto.description() != null){
            article.setDescription(dto.description());
        }
        if(dto.content() != null){
            article.setContent(dto.content());
        }
        if(dto.publishedAt() != null){
            article.setPublishedAt(dto.publishedAt());
        }
        if(dto.categoryId() != null && !dto.categoryId().equals(article.getCategory().getId())){
            ArticleCategory articleCategory = articleCategoryRepository.findById(dto.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("articleCategory.notfound"));
            article.setCategory(articleCategory);
        }

        return articleMapper.toDto(articleRepository.save(article));
    }

    @Override
    public void deleteArticle(Long id) {
        Objects.requireNonNull(id);
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("article.notfound"));

        checkOwnership(article.getAuthor().getEmail());

        articleRepository.delete(article);
    }

    @Override
    public void likeArticle(Long id,String email) {
        Objects.requireNonNull(id);
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("article.notfound"));
        User connectedUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("user.notfound"));

        if(articleLikeRepository.existsByArticle_IdAndUser_Id(connectedUser.getId(),article.getId())){
            throw new ResourceAlreadyExistsException("like.exists");
        }

        ArticleLike articleLike = new ArticleLike();
        articleLike.setArticle(article);
        articleLike.setUser(connectedUser);
        articleLikeRepository.save(articleLike);
        articleRepository.incrementLikeCount(id);
    }

    @Override
    public void unlikeArticle(Long id,String email) {
        Objects.requireNonNull(id);
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("article.notfound"));
        User connectedUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("user.notfound"));

        ArticleLike articleLike = articleLikeRepository.findByUser_IdAndArticle_Id(connectedUser.getId(),article.getId())
                .orElseThrow(()->new ResourceNotFoundException("like.notfound"));

        articleLikeRepository.delete(articleLike);
        articleRepository.decrementLikeCount(id);
    }

    @Override
    public ArticleDto getArticleBySlug(String slug) {
        Objects.requireNonNull(slug);

        return articleMapper.toDto(articleRepository.findBySlug(slug)
                .orElseThrow(()-> new ResourceNotFoundException("article.notfound")));
    }

    @Override
    public Page<ArticleDto> findByCategoryId(Long categoryId, Pageable pageable) {
        Objects.requireNonNull(categoryId);

        ArticleCategory articleCategory = articleCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("articleCategory.notfound"));

        return articleRepository.findByCategory_Id(categoryId,pageable)
                .map(articleMapper::toDto);
    }

    private String generateUniqueSlug(String title){
        String baseSlug = SlugUtil.toSlug(title);
        String slug = baseSlug;
        int count = 1;
        while(articleRepository.existsBySlug(slug)){
            slug = baseSlug + "-" + count++;
        }
        return slug;
    }

    private void checkOwnership(String authorEmail) {
        String connectedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        var authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        boolean isAdmin = authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));

        if (!connectedEmail.equals(authorEmail) && !isAdmin) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à modifier cette ressource.");
        }
    }
}
