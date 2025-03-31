package com.example.articles.service;

import com.example.articles.entities.Article;
import java.util.List;
import java.util.Optional;

public interface ArticleService {
    List<Article> findAll();
    List<Article> getAllArticles();
    Optional<Article> getArticleById(Long id);
    Article createArticle(Article article);
    Article updateArticle(Long id, Article updatedArticle);
    void deleteArticle(Long id);
    List<Article> getArticlesByAuthor(Long authorId);
    List<Article> getArticlesByTag(Long tagId);
    List<Article> searchArticles(String query);

}
