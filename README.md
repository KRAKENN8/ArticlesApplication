Название
ArticlesApplication

7. Создайте несколько методов в репозиториях, используя @Query аннотацию. Например, найдите все статьи, опубликованные в определенном месяце. 

2. Определите маршруты для контроллеров 
ArticleController 
GET /articles - Показать список всех статей 
GET/articles/{id} — Показать статью по ID 
POST/articles - Добавить новую статью 
PUT/articles/{id} — Обновить статью 
DELETE/articles/{id} — Удалить статью 
GET/articles/by-author/{authorld) - Показать статьи по автору 
GET/articles/by-tag/{tagld} - Показать статьи по тегу 
GET/articles/search- Поиск статей по заголовку или содержимому 
TagController 
GET /tags - Показать все теги 
POST /tags - Добавить тег 
DELETE/tags/{id} - Удалить тег 
UserController 
GET /users - Показать всех пользователей 
GET/users/{id} — Показать пользователя по ID 
POST/users - Добавить нового пользователя 
PUT/users/{id}- Обновить данные пользователя 
DELETE/users/{id} - Удалить пользователя

GitHub 
В файле README.md нужно описать проект и его структуру, чтобы другие разработчики могли легко его развернуть и понять. 
Название проекта и описание 
Структура проекта (Описание директорий проекта) 
Как запустить проект 
База данных (ER-диаграмма) 
Функциональность 
Роли пользователей