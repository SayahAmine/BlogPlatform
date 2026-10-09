# Blog Platform - System Architecture
#Use Case Diagram
<img width="1132" height="848" alt="Screenshot From 2026-10-09 14-17-46" src="https://github.com/user-attachments/assets/7f512890-1b45-4386-a7a7-3ef7e31619ae" />

# Class Diagram

This diagram represents the core entities and their relationships in the database.
<img width="2097" height="2914" alt="deepseek_mermaid_20261009_e39642" src="https://github.com/user-attachments/assets/1f36298c-caf6-4321-9149-c2fb2d933ccb" />

## 🌐 Available APIs

All endpoints are prefixed with `/api/v1`.

###  Authentication (Public)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/auth/register` | Register a new user. |
| `POST` | `/auth/login` | Login and receive a JWT token. |

###  Posts (Protected)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/posts` | Get all posts (optional filters: `?categoryId=...&tagId=...`). |
| `GET` | `/posts/{id}` | Get a single post by ID. |
| `GET` | `/posts/drafts` | Get the current user's draft posts. |
| `POST` | `/posts` | Create a new post. |
| `PATCH` | `/posts/{id}` | Update a post (partial update). |
| `DELETE` | `/posts/{id}` | Delete a post (only the author can delete). |

###  Categories (Protected)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/categories` | List all categories. |
| `POST` | `/categories` | Create a new category. |
| `DELETE` | `/categories/{id}` | Delete a category (must have no posts). |

###  Tags (Protected)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/tags` | List all tags. |
| `POST` | `/tags` | Create multiple tags at once. |
| `DELETE` | `/tags/{id}` | Delete a tag (must have no posts). |

##  Security Overview

This application uses **JWT (JSON Web Token)** based authentication. Here is how it works:

1. **Stateless Architecture:** The server does not store session data. Every request must contain a valid JWT in the `Authorization` header (`Bearer <token>`).
2. **Password Hashing:** User passwords are encrypted using **BCrypt** before being stored in the database.
3. **Public vs. Protected Routes:**
   * `/api/v1/auth/**` and the frontend static files (`/`, `/login`, `/css/**`, `/js/**`) are public.
   * All other endpoints require a valid token.
4. **JWT Filter (`JwtAuthenticationFilter`):** This custom filter intercepts every request, extracts the token, validates it, and sets the user in the Spring Security Context.
5. **Authorization:** Users can only edit or delete their **own** posts. Attempting to modify someone else's post will return a `403 Forbidden` error.
