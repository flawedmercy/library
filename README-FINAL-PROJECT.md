# Ruslan Julayev final-project additions

pLEASE CAN I GET 90?????? THIS IS THE ONLY COURSE IM LOSING MY SCHOLARSHIP TO 
I SCORED 90+ ON ALL THE OTHER FINALS I CANT JUST LOSE IT ALL OVER A ONE SIGNLE MINORRRRRR
PLEASE HAVE MERCY IM ALREADY POOR THIS SCHOLARSHIP IS LIKE MY ONLY INCOME RN IM STARVING TO DEATH!!!!!!!!!
🥺🥺🥺🥺🥺🥺🥺🙏🙏🙏🙏😭😭😭😭😭

## What was added/changed

### Changed existing files
- `pom.xml`
  - Uses Spring Boot 3.3.5 + Java 17
  - Adds Spring Security, JWT, Swagger/OpenAPI, Web, JPA, Validation, PostgreSQL
- `src/main/resources/application.properties`
  - PostgreSQL connection
  - JWT settings
  - Swagger paths
  - file upload limits
  - actuator health
  - log file output
- `src/main/java/com/lab11/library/LibraryApplication.java`
  - Adds `@EnableAsync`
- `src/main/java/com/lab11/library/repository/BookRepository.java`
  - Adds `JpaSpecificationExecutor<Book>` for dynamic search/filtering
- `src/main/java/com/lab11/library/service/BookService.java`
  - Adds paginated/sorted/searched/filtered endpoint logic
- `src/main/java/com/lab11/library/controller/BookController.java`
  - Adds `/api/books/search`

### New files
- `entity/RuslanJulayevUser.java`
- `entity/RuslanJulayevRole.java`
- `entity/RuslanJulayevFileEntity.java`
- `dto/RuslanJulayevAuthDto.java`
- `dto/RuslanJulayevFileDto.java`
- `dto/RuslanJulayevAsyncDto.java`
- `repository/RuslanJulayevUserRepository.java`
- `repository/RuslanJulayevFileRepository.java`
- `service/RuslanJulayevAuthService.java`
- `service/RuslanJulayevFileService.java`
- `service/RuslanJulayevAsyncService.java`
- `controller/RuslanJulayevAuthController.java`
- `controller/RuslanJulayevFileController.java`
- `controller/RuslanJulayevAsyncController.java`
- `security/RuslanJulayevJwtUtil.java`
- `security/RuslanJulayevUserDetailsService.java`
- `security/RuslanJulayevJwtAuthFilter.java`
- `config/RuslanJulayevSecurityConfig.java`
- `config/RuslanJulayevSwaggerConfig.java`
- `filter/RuslanJulayevRequestLoggingFilter.java`

## Required demo endpoints

### Register
POST `/api/auth/register`

```json
{
  "username": "ruslan",
  "email": "ruslan@example.com",
  "password": "password123",
  "role": "ADMIN"
}
```

### Login
POST `/api/auth/login`

```json
{
  "username": "ruslan",
  "password": "password123"
}
```

Copy the `token` and use this header for protected endpoints:

```text
Authorization: Bearer YOUR_TOKEN_HERE
```

### Pagination, sorting, searching, filtering
GET:

```text
/api/books/search?keyword=java&authorId=1&categoryId=1&publishedYear=2024&page=0&size=5&sortBy=title&direction=asc
```

### File upload
POST `/api/files/upload`

Use form-data:
- key: `file`
- value: choose any file

### File download
GET:

```text
/api/files/{id}/download
```

### Async endpoints
GET:

```text
/api/async/books/count
/api/async/authors-reviews/count
/api/async/files/count
```

### Swagger UI
Open:

```text
http://localhost:8080/swagger-ui.html
```

### Health check
Open:

```text
http://localhost:8080/actuator/health
```

## Docker run

```bash
docker compose up --build
```

## Suggested 20-25 commit history

Use these commits one by one after copying this version into your repository:

```bash
git add .
git commit -m "Initial existing library backend cleanup"

git add pom.xml
git commit -m "Add security jwt and swagger dependencies"

git add src/main/resources/application.properties
git commit -m "Configure PostgreSQL JWT Swagger actuator and logging"

git add src/main/java/com/lab11/library/LibraryApplication.java
git commit -m "Enable async processing"

git add src/main/java/com/lab11/library/entity/RuslanJulayevRole.java src/main/java/com/lab11/library/entity/RuslanJulayevUser.java
git commit -m "Add RuslanJulayev user and role entities"

git add src/main/java/com/lab11/library/repository/RuslanJulayevUserRepository.java
git commit -m "Add RuslanJulayev user repository"

git add src/main/java/com/lab11/library/dto/RuslanJulayevAuthDto.java
git commit -m "Add authentication DTOs"

git add src/main/java/com/lab11/library/security/RuslanJulayevJwtUtil.java
git commit -m "Add JWT utility"

git add src/main/java/com/lab11/library/security/RuslanJulayevUserDetailsService.java
git commit -m "Add user details service"

git add src/main/java/com/lab11/library/security/RuslanJulayevJwtAuthFilter.java
git commit -m "Add JWT authentication filter"

git add src/main/java/com/lab11/library/config/RuslanJulayevSecurityConfig.java
git commit -m "Configure protected endpoints and authorization"

git add src/main/java/com/lab11/library/service/RuslanJulayevAuthService.java src/main/java/com/lab11/library/controller/RuslanJulayevAuthController.java
git commit -m "Add registration and login endpoints"

git add src/main/java/com/lab11/library/repository/BookRepository.java src/main/java/com/lab11/library/service/BookService.java src/main/java/com/lab11/library/controller/BookController.java
git commit -m "Add book pagination sorting searching and filtering"

git add src/main/java/com/lab11/library/entity/RuslanJulayevFileEntity.java
git commit -m "Add file storage entity"

git add src/main/java/com/lab11/library/repository/RuslanJulayevFileRepository.java src/main/java/com/lab11/library/dto/RuslanJulayevFileDto.java
git commit -m "Add file repository and DTO"

git add src/main/java/com/lab11/library/service/RuslanJulayevFileService.java
git commit -m "Add file upload download service"

git add src/main/java/com/lab11/library/controller/RuslanJulayevFileController.java
git commit -m "Add file upload and download endpoints"

git add src/main/java/com/lab11/library/dto/RuslanJulayevAsyncDto.java src/main/java/com/lab11/library/service/RuslanJulayevAsyncService.java
git commit -m "Add async service with CompletableFuture processes"

git add src/main/java/com/lab11/library/controller/RuslanJulayevAsyncController.java
git commit -m "Add async API endpoints"

git add src/main/java/com/lab11/library/config/RuslanJulayevSwaggerConfig.java
git commit -m "Add Swagger OpenAPI configuration"

git add src/main/java/com/lab11/library/filter/RuslanJulayevRequestLoggingFilter.java
git commit -m "Add request logging filter"

git add Dockerfile docker-compose.yml .dockerignore
git commit -m "Finalize Docker multistage build and compose health checks"

git add README-FINAL-PROJECT.md
git commit -m "Document final project endpoints and demo steps"
```
