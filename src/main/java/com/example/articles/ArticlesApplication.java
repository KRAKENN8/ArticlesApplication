package com.example.articles;

import com.example.articles.entities.Article;
import com.example.articles.entities.ArticleComment;
import com.example.articles.entities.ArticleFavorite;
import com.example.articles.entities.Tag;
import com.example.articles.entities.User;
import com.example.articles.repositories.ArticleCommentRepository;
import com.example.articles.repositories.ArticleFavoriteRepository;
import com.example.articles.repositories.ArticleRepository;
import com.example.articles.repositories.TagRepository;
import com.example.articles.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class ArticlesApplication implements CommandLineRunner {

	@Autowired
	private ArticleCommentRepository articleCommentRepository;

	@Autowired
	private ArticleFavoriteRepository articleFavoriteRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(ArticlesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		int choice = -1;
		while (choice != 0) {
			printMenu();
			try {
				choice = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Неверный ввод. Пожалуйста, введите число.");
				continue;
			}

			switch (choice) {
				case 0:
					System.out.println("Выход из программы.");
					break;
				case 1:
					System.out.print("Введите id статьи (article_id): ");
					Long articleId = Long.parseLong(scanner.nextLine());
					List<ArticleComment> commentsByArticle = articleCommentRepository.findByArticleId(articleId);
					System.out.println("Комментарии для статьи с id " + articleId + ": " + commentsByArticle);
					break;
				case 2:
					System.out.print("Введите id пользователя (user_id): ");
					Long userId = Long.parseLong(scanner.nextLine());
					List<ArticleComment> commentsByUser = articleCommentRepository.findByUserId(userId);
					System.out.println("Комментарии пользователя с id " + userId + ": " + commentsByUser);
					break;
				case 3:
					System.out.print("Введите id пользователя (user_id): ");
					Long userIdForFav = Long.parseLong(scanner.nextLine());
					List<ArticleFavorite> favoritesByUser = articleFavoriteRepository.findByUserId(userIdForFav);
					System.out.println("Лайки пользователя с id " + userIdForFav + ": " + favoritesByUser);
					break;
				case 4:
					System.out.print("Введите id статьи (article_id): ");
					Long articleIdForFav = Long.parseLong(scanner.nextLine());
					List<ArticleFavorite> favoritesByArticle = articleFavoriteRepository.findByArticleId(articleIdForFav);
					System.out.println("Лайки для статьи с id " + articleIdForFav + ": " + favoritesByArticle);
					break;
				case 5:
					System.out.print("Введите полное название статьи (title): ");
					String fullTitle = scanner.nextLine();
					List<Article> articlesByTitle = articleRepository.findByTitle(fullTitle);
					System.out.println("Статьи с названием '" + fullTitle + "': " + articlesByTitle);
					break;
				case 6:
					System.out.print("Введите часть названия статьи (title): ");
					String titlePart = scanner.nextLine();
					List<Article> articlesByTitlePart = articleRepository.findByTitleContainingIgnoreCase(titlePart);
					System.out.println("Статьи, содержащие '" + titlePart + "' в названии: " + articlesByTitlePart);
					break;
				case 7:
					System.out.print("Введите имя тега (name): ");
					String tagName = scanner.nextLine();
					List<Article> articlesByTag = articleRepository.findByTags_Name(tagName);
					System.out.println("Статьи с тегом '" + tagName + "': " + articlesByTag);
					break;
				case 8:
					System.out.print("Введите id автора (author_id): ");
					Long authorId = Long.parseLong(scanner.nextLine());
					// Так как поле author в Article имеет тип User, ищем пользователя по id
					Optional<User> authorOpt = userRepository.findById(authorId);
					if (authorOpt.isPresent()) {
						List<Article> articlesByAuthor = articleRepository.findByAuthor_Id(authorId);
						System.out.println("Статьи автора " + authorOpt.get().getUsername() + ": " + articlesByAuthor);
					} else {
						System.out.println("Автор с id " + authorId + " не найден.");
					}
					break;
				case 9:
					System.out.print("Введите точное имя тега (name): ");
					String exactTag = scanner.nextLine();
					Tag tag = tagRepository.findByName(exactTag);
					System.out.println("Тег с именем '" + exactTag + "': " + tag);
					break;
				case 10:
					System.out.print("Введите часть имени тега (name): ");
					String tagPart = scanner.nextLine();
					List<Tag> tags = tagRepository.findByNameContainingIgnoreCase(tagPart);
					System.out.println("Теги, содержащие '" + tagPart + "': " + tags);
					break;
				case 11:
					System.out.print("Введите email пользователя (email): ");
					String email = scanner.nextLine();
					Optional<User> userByEmail = userRepository.findByEmail(email);
					System.out.println("Пользователь с email '" + email + "': " + userByEmail.orElse(null));
					break;
				case 12:
					System.out.print("Введите username пользователя (username): ");
					String username = scanner.nextLine();
					User userByUsername = userRepository.findByUsername(username);
					System.out.println("Пользователь с username '" + username + "': " + userByUsername);
					break;
				case 13:
					System.out.print("Введите часть email для поиска (email): ");
					String emailPart = scanner.nextLine();
					List<User> usersByEmailPart = userRepository.findByEmailContainingIgnoreCase(emailPart);
					System.out.println("Пользователи, email которых содержит '" + emailPart + "': " + usersByEmailPart);
					break;
				case 14:
					System.out.print("Введите часть username для поиска (username): ");
					String usernamePart = scanner.nextLine();
					List<User> usersByUsernamePart = userRepository.findByUsernameContainingIgnoreCase(usernamePart);
					System.out.println("Пользователи, username которых содержит '" + usernamePart + "': " + usersByUsernamePart);
					break;
				case 15:
					System.out.print("Введите часть email для поиска (email): ");
					String emailSearch = scanner.nextLine();
					System.out.print("Введите часть username для поиска (username): ");
					String usernameSearch = scanner.nextLine();
					List<User> combinedUsers = userRepository.findByEmailContainingIgnoreCaseOrUsernameContainingIgnoreCase(emailSearch, usernameSearch);
					System.out.println("Пользователи по комбинированному поиску: " + combinedUsers);
					break;
				default:
					System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
			}
			System.out.println(); // пустая строка для разделения выводов
		}
		scanner.close();
	}

	private void printMenu() {
		System.out.println("---------- Меню ----------");
		System.out.println("0. Выход");
		System.out.println("1. Получить комментарии к статье по id");
		System.out.println("2. Получить комментарии пользователя по id");
		System.out.println("3. Получить лайки пользователя по id");
		System.out.println("4. Получить лайки статьи по id");
		System.out.println("5. Найти статьи по полному названию");
		System.out.println("6. Найти статьи по части названия");
		System.out.println("7. Найти статьи по тегу");
		System.out.println("8. Найти статьи по автору");
		System.out.println("9. Найти тег по точному имени");
		System.out.println("10. Найти теги по части имени");
		System.out.println("11. Найти пользователя по email");
		System.out.println("12. Найти пользователя по username");
		System.out.println("13. Найти пользователей, email которых содержит часть");
		System.out.println("14. Найти пользователей, username которых содержит часть");
		System.out.println("15. Комбинированный поиск пользователей (email или username)");
		System.out.print("Выберите пункт меню: ");
	}
}
