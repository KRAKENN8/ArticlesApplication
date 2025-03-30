package com.example.articles.repositories;

import com.example.articles.entities.Article;
import com.example.articles.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    // Поиск статьи по полному названию
    List<Article> findByTitle(String title);

    // Поиск статей, в названии которых содержится указанный отрывок (без учета регистра)
    List<Article> findByTitleContainingIgnoreCase(String titlePart);

    // Поиск статей по тегу (по имени тега)
    List<Article> findByTags_Name(String tagName);

    // Поиск статей по автору (при передаче объекта User)
    List<Article> findByAuthor(User author);

    // Поиск статей по автору по id
    List<Article> findByAuthor_Id(Long authorId);

    // Поиск статей по тегу по id
    List<Article> findByTags_Id(Long tagId);
}
