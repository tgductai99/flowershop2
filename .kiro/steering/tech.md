# Technology Stack

## Core Framework
- **Spring Boot 3.2.2** - Main application framework
- **Java 17** - Programming language
- **Maven** - Build system and dependency management

## Web Layer
- **Spring MVC** - Web framework
- **Thymeleaf** - Server-side templating engine
- **Thymeleaf Layout Dialect** - Layout management
- **Spring Security** - Authentication and authorization
- **Thymeleaf Spring Security Integration** - Security in templates

## Data Layer
- **Spring Data JPA** - Data access abstraction
- **Hibernate** - ORM implementation
- **Microsoft SQL Server** - Database
- **Bean Validation** - Input validation (@NotBlank, @Valid, @Positive)

## Development Tools
- **Lombok** - Reduces boilerplate code
- **Spring Boot DevTools** - Hot reload during development
- **Spring Boot Test** - Testing framework

## Common Commands

### Build and Run
```bash
# Clean and compile
mvn clean compile

# Run the application
mvn spring-boot:run

# Package as WAR
mvn clean package

# Run tests
mvn test
```

### Development
- Application runs on default port 8080
- Context path is set to root (`/`)
- Hot reload enabled with DevTools
- Thymeleaf cache disabled for development

## Database Configuration
- Uses SQL Server with database name `AsmJava5`
- Default connection: `localhost:1433`
- Hibernate DDL auto set to `none` (manual schema management)
- SQL logging enabled for development