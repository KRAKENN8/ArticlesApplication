package com.example.articles.service;

import com.example.articles.entities.Author;
import java.util.List;

public interface AuthorService {
    List<Author> getAllAuthors();
    Author getAuthorById(Long id);
}
