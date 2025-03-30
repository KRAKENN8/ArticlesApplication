package com.example.articles.controllers;

import com.example.articles.entities.Article;
import com.example.articles.entities.Author;
import com.example.articles.entities.Tag;
import com.example.articles.entities.User;
import com.example.articles.repositories.UserRepository;
import com.example.articles.service.ArticleService;
import com.example.articles.service.AuthorService;
import com.example.articles.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final AuthorService authorService;
    private final TagService tagService;
    private final UserRepository userRepository;

    @Autowired
    public ArticleController(ArticleService articleService,
                             AuthorService authorService,
                             TagService tagService,
                             UserRepository userRepository) {
        this.articleService = articleService;
        this.authorService = authorService;
        this.tagService = tagService;
        this.userRepository = userRepository;
    }

    // Вывод списка статей
    @GetMapping
    public String listArticles(Model model) {
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        return "articles/list"; // Шаблон: src/main/resources/templates/articles/list.html
    }

    // Отображение деталей статьи
    @GetMapping("/{id}")
    public String articleDetails(@PathVariable Long id, Model model) {
        Article article = articleService.getArticleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with id " + id));
        model.addAttribute("article", article);
        return "articles/details"; // Шаблон: src/main/resources/templates/articles/details.html
    }

    // Форма для создания новой статьи
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("article", new Article());
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("tags", tagService.getAllTags());
        return "articles/add"; // Шаблон: src/main/resources/templates/articles/add.html
    }

    // Обработка создания новой статьи
    @PostMapping
    public String createArticle(@ModelAttribute("article") Article article,
                                @RequestParam("authorId") Long authorId,
                                @RequestParam("tagIds") List<Long> tagIds) {
        // Получаем автора по id и устанавливаем
        Author author = authorService.getAuthorById(authorId);
        article.setAuthor(author);

        // Получаем выбранные теги
        Set<Tag> tags = new HashSet<>();
        for (Long tagId : tagIds) {
            Tag tag = tagService.getTagById(tagId);
            tags.add(tag);
        }
        article.setTags(tags);

        // Получаем текущего аутентифицированного пользователя и устанавливаем как владельца
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userRepository.findByUsername(username);
        article.setOwner(currentUser);

        articleService.createArticle(article);
        return "redirect:/articles";
    }

    // Форма для редактирования статьи
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Article article = articleService.getArticleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with id " + id));

        // Проверяем, имеет ли текущий пользователь право редактировать статью
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userRepository.findByUsername(username);
        if (!currentUser.getRole().equals(User.Roles.ADMIN_ROLE) &&
                (article.getOwner() == null || !article.getOwner().getId().equals(currentUser.getId()))) {
            return "redirect:/access-denied";
        }

        model.addAttribute("article", article);
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("tags", tagService.getAllTags());
        return "articles/edit"; // Шаблон: src/main/resources/templates/articles/edit.html
    }

    // Обработка обновления статьи
    @PostMapping("/update/{id}")
    public String updateArticle(@PathVariable Long id,
                                @ModelAttribute("article") Article updatedArticle,
                                @RequestParam("authorId") Long authorId,
                                @RequestParam("tagIds") List<Long> tagIds) {
        // Получаем существующую статью для проверки прав
        Article existingArticle = articleService.getArticleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with id " + id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userRepository.findByUsername(username);
        if (!currentUser.getRole().equals(User.Roles.ADMIN_ROLE) &&
                (existingArticle.getOwner() == null || !existingArticle.getOwner().getId().equals(currentUser.getId()))) {
            return "redirect:/access-denied";
        }

        // Устанавливаем автора
        Author author = authorService.getAuthorById(authorId);
        updatedArticle.setAuthor(author);

        // Устанавливаем теги
        Set<Tag> tags = new HashSet<>();
        for (Long tagId : tagIds) {
            Tag tag = tagService.getTagById(tagId);
            tags.add(tag);
        }
        updatedArticle.setTags(tags);

        // Сохраняем владельца из существующей статьи, чтобы не перезаписывать его
        updatedArticle.setOwner(existingArticle.getOwner());

        // В сервисном слое обновляем только скалярные поля и ассоциации, оставляя коллекции (comments, favorites) неизменными
        articleService.updateArticle(id, updatedArticle);
        return "redirect:/articles";
    }

    // Удаление статьи
    @GetMapping("/delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        Article article = articleService.getArticleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with id " + id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userRepository.findByUsername(username);
        if (!currentUser.getRole().equals(User.Roles.ADMIN_ROLE) &&
                (article.getOwner() == null || !article.getOwner().getId().equals(currentUser.getId()))) {
            return "redirect:/access-denied";
        }

        articleService.deleteArticle(id);
        return "redirect:/articles";
    }
}
