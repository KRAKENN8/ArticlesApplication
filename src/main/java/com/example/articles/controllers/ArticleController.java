package com.example.articles.controllers;

import com.example.articles.entities.*;
import com.example.articles.repositories.ArticleCommentRepository;
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

import java.time.LocalDateTime;
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
    private final ArticleCommentRepository articleCommentRepository;

    @Autowired
    public ArticleController(ArticleService articleService,
                             AuthorService authorService,
                             TagService tagService,
                             UserRepository userRepository,
                             ArticleCommentRepository articleCommentRepository) {
        this.articleService = articleService;
        this.authorService = authorService;
        this.tagService = tagService;
        this.userRepository = userRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @GetMapping
    public String listArticles(Model model) {
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Если пользователь не аутентифицирован, либо это anonymousUser – передаём null
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            model.addAttribute("currentUser", null);
        } else {
            // Иначе находим пользователя по имени
            User currentUser = userRepository.findByUsername(auth.getName());
            model.addAttribute("currentUser", currentUser);
        }
        return "articles/list";
    }

    // Отображение деталей статьи
    @GetMapping("/{id}")
    public String articleDetails(@PathVariable Long id, Model model) {
        Article article = articleService.getArticleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with id " + id));
        model.addAttribute("article", article);

        // Добавляем текущего пользователя (или null, если гость)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            currentUser = userRepository.findByUsername(auth.getName());
        }
        model.addAttribute("currentUser", currentUser);

        return "articles/details";
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

    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable Long id, @RequestParam("body") String body) {
        // Получаем статью по id
        Article article = articleService.getArticleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with id " + id));

        // Получаем текущего пользователя
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";
        }
        User currentUser = userRepository.findByUsername(auth.getName());

        // Создаем новый комментарий
        ArticleComment comment = new ArticleComment();
        comment.setBody(body);
        comment.setArticle(article);
        comment.setUser(currentUser);
        comment.setCreatedAt(LocalDateTime.now().withSecond(0).withNano(0));
        comment.setUpdatedAt(LocalDateTime.now().withSecond(0).withNano(0));

        articleCommentRepository.save(comment);

        // При необходимости можно обновить коллекцию комментариев статьи,
        // но если связь настроена с cascade, то комментарий будет автоматически подхвачен
        return "redirect:/articles/" + id;
    }

    @GetMapping("/search")
    public String searchArticles(@RequestParam("query") String query, Model model) {
        List<Article> articles = articleService.searchArticles(query);
        List<Author> authors = authorService.getAllAuthors();  // Получаем всех авторов
        List<Tag> tags = tagService.getAllTags();  // Получаем все теги

        model.addAttribute("articles", articles);
        model.addAttribute("authors", authors);  // Передаем авторов в шаблон
        model.addAttribute("tags", tags);  // Передаем теги в шаблон
        model.addAttribute("searchQuery", query);  // Передаем запрос в шаблон
        return "articles/list";  // Перенаправляем на страницу списка статей
    }

    // Показать статьи по автору
    @GetMapping("/by-author/{authorId}")
    public String getArticlesByAuthor(@PathVariable Long authorId, Model model) {
        List<Article> articles = articleService.getArticlesByAuthor(authorId);
        model.addAttribute("articles", articles);

        // Добавляем текущего пользователя в модель
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            currentUser = userRepository.findByUsername(auth.getName());
        }
        model.addAttribute("currentUser", currentUser);

        return "articles/list"; // Этот шаблон будет отображать список статей
    }

    // Показать статьи по тегу
    @GetMapping("/by-tag/{tagId}")
    public String getArticlesByTag(@PathVariable Long tagId, Model model) {
        List<Article> articles = articleService.getArticlesByTag(tagId);
        model.addAttribute("articles", articles);

        // Добавляем текущего пользователя в модель
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            currentUser = userRepository.findByUsername(auth.getName());
        }
        model.addAttribute("currentUser", currentUser);

        return "articles/list"; // Этот шаблон будет отображать список статей
    }
}
