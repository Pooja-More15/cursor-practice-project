# JWT login (Java 21)

A small Spring Boot app that logs a user in and returns a JWT. Protected APIs need that token in the `Authorization` header.

## Versions

- Java 21
- Spring Boot 4.1.2 (current stable for this JDK)

## How to run

Maven is not required on your PATH. Use the wrapper:

```
.\mvnw.cmd spring-boot:run
```

Then open http://localhost:8080

Demo login:

- username: `demo`
- password: `password123`

## What happens on login

1. `POST /api/auth/login` checks the username and password.
2. The server signs a JWT (HS256) and returns it.
3. `GET /api/me` requires `Authorization: Bearer <token>`.

You can also register a new user with `POST /api/auth/register`.
