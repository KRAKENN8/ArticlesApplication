package com.example.articles.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user") // Таблица называется "users"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;
    private String email;
    private String username;
    private String imageUrl;
    private String password;

    private Roles role = Roles.USER_ROLE;

    public enum Roles {
        USER_ROLE,
        ADMIN_ROLE
    }

    @Column(columnDefinition = "TEXT")
    private String bio;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleFavorite> favorites = new ArrayList<>();

    // Getters / Setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public List<ArticleComment> getComments() {
        return comments;
    }
    public void setComments(List<ArticleComment> comments) {
        this.comments = comments;
    }
    public List<ArticleFavorite> getFavorites() {
        return favorites;
    }
    public void setFavorites(List<ArticleFavorite> favorites) {
        this.favorites = favorites;
    }

    // Новый геттер для поля role
    public Roles getRole() {
        return role;
    }

    // При необходимости можно добавить и сеттер для поля role
    public void setRole(Roles role) {
        this.role = role;
    }
}
