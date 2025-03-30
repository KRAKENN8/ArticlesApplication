package com.example.articles.service;

import com.example.articles.entities.Tag;
import java.util.List;

public interface TagService {
    List<Tag> getAllTags();
    Tag getTagById(Long id);
}
