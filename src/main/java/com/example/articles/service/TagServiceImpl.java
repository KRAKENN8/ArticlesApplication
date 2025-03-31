package com.example.articles.service;

import com.example.articles.entities.Tag;
import com.example.articles.repositories.TagRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Тег не найден"));
    }

    @Override
    public Tag createTag(Tag tag) {
        // При создании устанавливаем текущую дату
        tag.setCreatedAt(LocalDateTime.now());
        return tagRepository.save(tag);
    }

    @Override
    public Tag updateTag(Long id, Tag tag) {
        return tagRepository.findById(id)
                .map(existingTag -> {
                    existingTag.setName(tag.getName());
                    // Если нужно обновлять и дату создания/обновления, добавьте соответствующие поля
                    return tagRepository.save(existingTag);
                })
                .orElseThrow(() -> new IllegalArgumentException("Тег не найден"));
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
}
