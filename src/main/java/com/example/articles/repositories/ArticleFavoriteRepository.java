package com.example.articles.repositories;

import com.example.articles.entities.ArticleFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArticleFavoriteRepository extends JpaRepository<ArticleFavorite, Long> {}
