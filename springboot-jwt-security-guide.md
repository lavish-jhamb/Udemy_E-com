# JWT Authentication & Authorization -- Spring Boot Guide

Environment: - Spring Boot: 4.x - Spring Framework: 7.x - Java: 25 -
Security: Spring Security 7 - Auth Type: Stateless JWT

------------------------------------------------------------------------

# 1. Dependencies

Purpose: Enable security, JWT generation, and password encryption.

``` xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.5</version>
</dependency>

<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.5</version>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.5</version>
    <scope>runtime</scope>
</dependency>
```

------------------------------------------------------------------------

# 2. Recommended Package Structure

    security
    │
    ├── config
    │   └── SecurityConfig
    │
    ├── jwt
    │   ├── JwtService
    │   ├── JwtAuthenticationFilter
    │
    ├── service
    │   └── CustomUserDetailsService
    │
    ├── model
    │   └── UserPrincipal
    │
    ├── controller
    │   └── AuthController
    │
    ├── dto
    │   ├── LoginRequest
    │   └── AuthResponse

------------------------------------------------------------------------

# 3. Class Responsibilities (Quick Revision)

  Class                      Purpose
  -------------------------- ------------------------------------------
  User                       Database entity storing user credentials
  UserRepository             Fetch user from database
  UserPrincipal              Adapter converting User → UserDetails
  CustomUserDetailsService   Loads user for authentication
  JwtService                 Generate and validate JWT tokens
  JwtAuthenticationFilter    Validates JWT for every request
  SecurityConfig             Configures Spring Security rules
  AuthController             Login endpoint issuing JWT
  LoginRequest               Login request payload
  AuthResponse               JWT response payload

------------------------------------------------------------------------

# 4. Authentication Flow

    Client
      |
      | Login Request
      v
    AuthController
      |
      v
    AuthenticationManager
      |
      v
    CustomUserDetailsService
      |
      v
    Database
      |
      v
    JWT Generated
      |
      v
    Client Receives Token

------------------------------------------------------------------------

# 5. Request Authorization Flow

    Client -> API Request
    Authorization: Bearer JWT

    JwtAuthenticationFilter
            |
            v
    JwtService validates token
            |
            v
    SecurityContext set
            |
            v
    Controller executes

------------------------------------------------------------------------

# 6. Example Endpoint Security

    /auth/**   -> permitAll
    /api/**    -> authenticated
    /admin/**  -> ADMIN role
    /user/**   -> USER or ADMIN role

------------------------------------------------------------------------

# 7. Best Practices

-   Use BCryptPasswordEncoder
-   Keep JWT secret in environment variables
-   Use short token expiration
-   Implement refresh tokens for production
-   Always use HTTPS
