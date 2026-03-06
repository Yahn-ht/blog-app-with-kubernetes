package com.blog.blogapp.controllers;

import com.blog.blogapp.consts.Consts;
import com.blog.blogapp.dtos.ArticleCategoryDto;
import com.blog.blogapp.dtos.CreateOrUpdateArticleCategoryDto;
import com.blog.blogapp.services.ArticleCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Consts.API_BASE + Consts.API_VERSION + "categories")
public class ArticleCategoryController {

     private final ArticleCategoryService categoryService;

     @PostMapping
     @PreAuthorize("hasAuthority('ADMIN')")
     public ResponseEntity<ArticleCategoryDto> createCategory(@Valid @RequestBody CreateOrUpdateArticleCategoryDto dto){
         ArticleCategoryDto categoryDto = categoryService.createCategory(dto);

         return ResponseEntity
                 .created(URI.create(Consts.API_BASE + Consts.API_VERSION + "categories/" +categoryDto.id() ))
                 .body(categoryDto);
     }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ArticleCategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CreateOrUpdateArticleCategoryDto dto){
        return new ResponseEntity<>(categoryService.updateCategory(id,dto), HttpStatus.OK);
    }

    @GetMapping
    private ResponseEntity<List<ArticleCategoryDto>> getAllCategories(@PageableDefault Pageable pageable){
         return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleCategoryDto> getCategoryById(@PathVariable Long id){
         return new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
         categoryService.deleteCategory(id);
         return new ResponseEntity<>(HttpStatus.OK);
    }

}
