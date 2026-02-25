# Project Structure

## Package Organization
Base package: `poly.edu`

### Core Application
- `AccessMotorApplication.java` - Main Spring Boot application class
- `ServletInitializer.java` - WAR deployment configuration

### Architecture Layers

#### Controllers (`controllers/`)
- Handle HTTP requests and responses
- Use `@Controller` annotation
- Follow RESTful URL patterns
- Example: `ProductController`, `AccountController`, `OrderController`

#### Models (`models/`)
- **Entities** (`entities/`) - JPA entities representing database tables
  - Use Lombok annotations (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`)
  - Validation annotations (`@NotBlank`, `@Positive`)
  - JPA annotations (`@Entity`, `@Table`, `@Id`, `@GeneratedValue`)

- **DTOs** (`dto/`) - Data Transfer Objects for API communication
- **DAOs** (`dao/`) - Data Access Objects with custom query methods
  - Interface-based with `InterfaceDAO<Entity, ID>` pattern
  - Implementation extends `AbstractDAO`
- **Repositories** (`repositories/`) - Spring Data JPA repositories
- **Services** (`services/`) - Business logic layer
  - Implement `ServiceInterface<Entity, ID>`
  - Use `@Service` and `@Transactional` annotations
- **Mappers** (`mappers/`) - Entity to DTO conversion

#### Configuration (`config/`)
- `SecurityConfig.java` - Spring Security configuration

#### Utilities (`utils/`)
- `ImageUtil.java` - Image file handling
- `PaginationUtil.java` - Pagination logic

#### Global Exception Handling (`advice/`)
- `GlobalAdvice.java` - Application-wide exception handling

## Resources Structure

### Templates (`src/main/resources/templates/`)
- Thymeleaf HTML templates
- `fragments/` - Reusable template fragments (header, footer, navbar, sidebar)
- `layouts/` - Page layout templates

### Static Assets (`src/main/resources/static/`)
- `images/` - Product images organized by category
  - `avatars/` - User profile images
  - `products/` - Product images by category (hoabo, hoacuoi, etc.)

## Naming Conventions

### Database
- Table names: PascalCase (e.g., `Products`, `Categories`)
- Column names: PascalCase (e.g., `Id`, `Name`, `CreateDate`)

### Java Classes
- Entities: Singular nouns (e.g., `Product`, `Category`, `Order`)
- Controllers: `{Entity}Controller` (e.g., `ProductController`)
- Services: `{Entity}Services` (e.g., `ProductServices`)
- DAOs: `{Entity}DAO` (e.g., `ProductDAO`)
- DTOs: `{Entity}DTO` (e.g., `ProductDTO`)

### URLs
- Dashboard routes: `/dashboard/{entity}`
- CRUD operations: `/dashboard/{entity}/{action}/{id?}`

## Key Patterns

### Service Layer Pattern
- Services implement `ServiceInterface<Entity, ID>`
- Standard CRUD operations: `save()`, `delete()`, `findById()`, `findAll()`
- Custom business logic methods (e.g., `filter()`, `findPage()`)

### DAO Pattern
- Interfaces extend `InterfaceDAO<Entity, ID>`
- Implementations extend `AbstractDAO<Entity, ID>`
- Custom query methods for complex operations

### MVC Pattern
- Controllers handle HTTP requests
- Services contain business logic
- DAOs/Repositories handle data access
- Thymeleaf templates for views