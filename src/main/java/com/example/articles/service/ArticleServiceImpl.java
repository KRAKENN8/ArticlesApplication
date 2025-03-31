package com.example.articles.service;

import com.example.articles.entities.Article;
import com.example.articles.repositories.ArticleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    public Article createArticle(Article article) {
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        return articleRepository.save(article);
    }

    @Override
    public Article updateArticle(Long id, Article updatedArticle) {
        return articleRepository.findById(id)
                .map(article -> {
                    // Обновляем скалярные поля
                    article.setTitle(updatedArticle.getTitle());
                    article.setDescription(updatedArticle.getDescription());
                    article.setSlug(updatedArticle.getSlug());
                    article.setBody(updatedArticle.getBody());
                    article.setUpdatedAt(LocalDateTime.now());
                    // Обновляем ассоциации (автор и теги)
                    article.setAuthor(updatedArticle.getAuthor());
                    article.setTags(updatedArticle.getTags());
                    // НЕ заменяем коллекции комментариев и избранного – оставляем их как есть
                    return articleRepository.save(article);
                })
                .orElseThrow(() -> new IllegalArgumentException("Article not found with id " + id));
    }

    @Override
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    @Override
    public List<Article> getArticlesByAuthor(Long authorId) {
        return articleRepository.findByAuthor_Id(authorId);
    }

    @Override
    public List<Article> getArticlesByTag(Long tagId) {
        return articleRepository.findByTags_Id(tagId);
    }

    @Override
    public List<Article> searchArticles(String query) {
        return articleRepository.findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(query, query);
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }
}
