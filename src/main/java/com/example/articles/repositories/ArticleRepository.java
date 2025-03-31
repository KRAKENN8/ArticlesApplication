package com.example.articles.repositories;

import com.example.articles.entities.Article;
import com.example.articles.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    // Поиск статей по автору по id
    List<Article> findByAuthor_Id(Long authorId);
    // Поиск статей по тегу по id
    List<Article> findByTags_Id(Long tagId);
    void deleteByOwnerId(Long ownerId);
}
