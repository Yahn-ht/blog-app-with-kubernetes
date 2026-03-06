package com.blog.blogapp.utils;

import com.blog.blogapp.domains.Article;
import com.blog.blogapp.domains.ArticleCategory;
import com.blog.blogapp.domains.User;
import com.blog.blogapp.enumeration.Role;
import com.blog.blogapp.repositories.ArticleCategoryRepository;
import com.blog.blogapp.repositories.ArticleRepository;
import com.blog.blogapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Order(1)
public class DatabaseSeeder implements CommandLineRunner {

    private final ArticleCategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // SEED CATEGORIES
        Map<String, ArticleCategory> categoryMap = new HashMap<>();
        if (categoryRepository.count() == 0) {
            List<String> categories = List.of("Tech", "Lifestyle", "Finance", "Health", "Travel");
            for (String name : categories) {
                ArticleCategory category = new ArticleCategory();
                category.setName(name);
                categoryRepository.save(category);
                categoryMap.put(name, category);
            }
            System.out.println("Categories seeded");
        } else {
            // load existing categories
            categoryRepository.findAll().forEach(c -> categoryMap.put(c.getName(), c));
        }

        // SEED USERS

        Map<String, User> userMap = new HashMap<>();
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@blog.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            userMap.put("admin", admin);

            User user = new User();
            user.setFirstName("Regular");
            user.setLastName("User");
            user.setEmail("user@blog.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole(Role.USER);
            userRepository.save(user);
            userMap.put("user", user);

            System.out.println("✅ 2 Users seeded (admin + user)");
        } else {
            userRepository.findAll().forEach(u -> userMap.put(u.getEmail(), u));
        }

        // SEED ARTICLES

        if (articleRepository.count() == 0) {
            articleRepository.saveAll(List.of(
                    new Article(
                            "Introduction à Spring Boot",
                            "Découverte de Spring Boot pour créer des APIs REST.",
                            "spring-boot-intro",
                            "Contenu complet sur Spring Boot...",
                            0,
                            LocalDateTime.now(),
                            userMap.get("admin"), // author
                            categoryMap.get("Tech"),
                            List.of(), List.of()
                    ),
                    new Article(
                            "Les bases de Java",
                            "Tout savoir sur les fondamentaux de Java.",
                            "java-basics",
                            "Contenu complet sur Java...",
                            0,
                            LocalDateTime.now(),
                            userMap.get("admin"),
                            categoryMap.get("Tech"),
                            List.of(), List.of()
                    ),
                    new Article(
                            "Healthy Living Tips",
                            "Conseils pour une vie saine et équilibrée.",
                            "healthy-living",
                            "Contenu sur la vie saine...",
                            0,
                            LocalDateTime.now(),
                            userMap.get("user"),
                            categoryMap.get("Lifestyle"),
                            List.of(), List.of()
                    ),
                    new Article(
                            "Financial Planning 101",
                            "Apprenez à gérer vos finances personnelles.",
                            "financial-planning",
                            "Contenu sur la finance...",
                            0,
                            LocalDateTime.now(),
                            userMap.get("user"),
                            categoryMap.get("Finance"),
                            List.of(), List.of()
                    ),
                    new Article(
                            "Top 10 Destinations 2026",
                            "Découvrez les voyages incontournables pour cette année.",
                            "top-destinations",
                            "Contenu sur les voyages...",
                            0,
                            LocalDateTime.now(),
                            userMap.get("user"),
                            categoryMap.get("Travel"),
                            List.of(), List.of()
                    ),
                    new Article(
                            "Yoga pour débutants",
                            "Guide pratique pour commencer le yoga.",
                            "yoga-beginners",
                            "Contenu sur le yoga...",
                            0,
                            LocalDateTime.now(),
                            userMap.get("user"),
                            categoryMap.get("Health"),
                            List.of(), List.of()
                    ),
                    new Article(
                            "Investir en bourse",
                            "Introduction à l'investissement en bourse pour débutants.",
                            "invest-bourse",
                            "Contenu sur l'investissement...",
                            0,
                            LocalDateTime.now(),
                            userMap.get("admin"),
                            categoryMap.get("Finance"),
                            List.of(), List.of()
                    ),
                    new Article(
                            "Minimalisme au quotidien",
                            "Comment adopter un style de vie minimaliste.",
                            "minimalism-daily",
                            "Contenu sur le minimalisme...",
                            0,
                            LocalDateTime.now(),
                            userMap.get("user"),
                            categoryMap.get("Lifestyle"),
                            List.of(), List.of()
                    ),
                    new Article(
                            "Frameworks Java populaires",
                            "Tour d’horizon des frameworks Java à connaître.",
                            "java-frameworks",
                            "Contenu sur les frameworks Java...",
                            0,
                            LocalDateTime.now(),
                            userMap.get("admin"),
                            categoryMap.get("Tech"),
                            List.of(), List.of()
                    ),
                    new Article(
                            "Voyager avec un petit budget",
                            "Astuces pour voyager moins cher et profiter pleinement.",
                            "budget-travel",
                            "Contenu sur voyager pas cher...",
                            0,
                            LocalDateTime.now(),
                            userMap.get("user"),
                            categoryMap.get("Travel"),
                            List.of(), List.of()
                    )
            ));
            System.out.println("10 Articles seeded");
        }
    }
}