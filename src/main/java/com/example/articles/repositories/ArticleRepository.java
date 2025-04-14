package com.example.articles.repositories;

import com.example.articles.entities.Article;
import com.example.articles.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByOwnerId(Long ownerId);
    List<Article> findByTags_Id(Long tagId);
    void deleteByOwnerId(Long ownerId);
    List<Article> findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(String title, String body);
}
