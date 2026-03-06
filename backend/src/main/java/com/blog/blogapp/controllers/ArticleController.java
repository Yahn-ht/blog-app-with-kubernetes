package com.blog.blogapp.controllers;

import com.blog.blogapp.consts.Consts;
import com.blog.blogapp.dtos.ArticleCreationDto;
import com.blog.blogapp.dtos.ArticleDto;
import com.blog.blogapp.dtos.UpdateArticleDto;
import com.blog.blogapp.services.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Consts.API_BASE + Consts.API_VERSION + "articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleDto> createArticle(Authentication authentication, @RequestBody @Valid ArticleCreationDto dto){
        String email = authentication.getName();
        ArticleDto articleDto = articleService.createArticle(email,dto);
        return ResponseEntity
                .created(URI.create(Consts.API_BASE + Consts.API_VERSION + "articles/" +articleDto.id() ))
                .body(articleDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getArticleById(@PathVariable Long id){
        return new ResponseEntity<>(articleService.getArticleById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ArticleDto>> getAllArticleDto(
                @PageableDefault Pageable pageable,
                @RequestParam(name = "searchKey", required = false) String searchKey,
                @RequestParam(name = "categoryId", required = false) Long categoryId
            ){
        return new ResponseEntity<>(articleService.findAll(pageable,searchKey,categoryId),HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ArticleDto>> getAllArticleDtoForUser(@PageableDefault Pageable pageable,@PathVariable Long userId){
        return new ResponseEntity<>(articleService.findAllArticleByUserId(userId,pageable),HttpStatus.OK);
    }

    @GetMapping("/user/mine")
    public ResponseEntity<Page<ArticleDto>> getAllConnectedUserArticles(@PageableDefault Pageable pageable,Authentication authentication){
        String email = authentication.getName();
        return new ResponseEntity<>(articleService.findUserConnectedArticle(email,pageable),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDto> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleDto articleDto){
        return new ResponseEntity<>(articleService.updateArticle(id,articleDto),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id){
        articleService.deleteArticle(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{articleId}/like")
    public ResponseEntity<Void> likeArticle(@PathVariable Long articleId, Authentication authentication){
        String email = authentication.getName();
        articleService.likeArticle(articleId,email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{articleId}/unlike")
    public ResponseEntity<Void> unlikeArticle(@PathVariable Long articleId, Authentication authentication){
        String email = authentication.getName();
        articleService.unlikeArticle(articleId,email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ArticleDto> getArticleBySlug(@PathVariable String slug){
        return new ResponseEntity<>(articleService.getArticleBySlug(slug),HttpStatus.OK);
    }
}
