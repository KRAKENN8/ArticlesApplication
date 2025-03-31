package com.example.articles.repositories;

import com.example.articles.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    void deleteById(Long id);  // Удаляет все записи с данным tagId
}