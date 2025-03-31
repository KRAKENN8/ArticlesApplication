package com.example.articles.repositories;

import com.example.articles.entities.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    void deleteByUserId(Long userId);
}
