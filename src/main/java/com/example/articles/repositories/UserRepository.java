package com.example.articles.repositories;

import com.example.articles.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Поиск пользователя по email
    Optional<User> findByEmail(String email);

    // Поиск пользователя по username
    User findByUsername(String username);

    // Поиск пользователей, email которых содержит заданную подстроку (без учета регистра)
    List<User> findByEmailContainingIgnoreCase(String emailPart);

    // Поиск пользователей, username которых содержит заданную подстроку (без учета регистра)
    List<User> findByUsernameContainingIgnoreCase(String usernamePart);

    // Комбинированный поиск по отрывку email или username (без учета регистра)
    List<User> findByEmailContainingIgnoreCaseOrUsernameContainingIgnoreCase(String emailPart, String usernamePart);
}
