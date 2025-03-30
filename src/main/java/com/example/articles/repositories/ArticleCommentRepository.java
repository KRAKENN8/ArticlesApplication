package com.example.articles.repositories;

import com.example.articles.entities.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {

    // Вывод комментариев к статье по её id
    List<ArticleComment> findByArticleId(Long articleId);

    // Вывод всех комментариев пользователя по его id
    List<ArticleComment> findByUserId(Long userId);
}
