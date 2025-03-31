package com.example.articles.repositories;

import com.example.articles.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    // Поиск тега по точному имени
    Tag findByName(String name);

    // Поиск тегов, имя которых содержит указанную подстроку (без учета регистра)
    List<Tag> findByNameContainingIgnoreCase(String namePart);
}
