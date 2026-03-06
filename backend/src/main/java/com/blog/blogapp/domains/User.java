package com.blog.blogapp.domains;

import com.blog.blogapp.enumeration.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User extends TraceableEntity implements UserDetails {

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private boolean deleted = false;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ArticleLike> articleLikes = new ArrayList<>();

    public void addComment(Article article, String content){
        Comment comment = new Comment();
        comment.setUser(this);
        comment.setArticle(article);
        comment.setContent(content);

        this.comments.add(comment);
        article.getComments().add(comment);
    }

    public void removeComment(Comment comment){
        if(this.comments.remove(comment)){
            comment.getArticle().getComments().remove(comment);
            comment.setUser(null);
            comment.setArticle(null);
        }
    }

//    public void addLike(Article article, String emoji){
//        ArticleLike articleLike = new ArticleLike();
//        articleLike.setEmoji(emoji);
//        articleLike.setUser(this);
//        articleLike.setArticle(article);
//
//        this.articleLikes.add(articleLike);
//        article.getArticleLikes().add(articleLike);
//    }
//
//    public void removeLike(Article article){
//        ArticleLike toRemove = this.articleLikes.stream()
//                .filter(articleLike -> articleLike.getArticle().equals(article))
//                .findFirst()
//                .orElse(null);
//
//        if(toRemove != null){
//            this.articleLikes.remove(toRemove);
//            article.getArticleLikes().remove(toRemove);
//            toRemove.setUser(null);
//            toRemove.setArticle(null);
//        }
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled() && !deleted;
    }
}
