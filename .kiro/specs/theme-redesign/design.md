# Design Document: Theme Redesign

## Overview

The theme redesign transforms the AccessMotor e-commerce application from its current appearance to a modern, beautiful, and simple design using Bootstrap 5.3 and custom CSS. The redesign maintains all existing functionality while significantly improving the visual presentation, user experience, and responsive behavior across all devices.

The design follows a layered approach:
1. **Foundation Layer**: Bootstrap 5.3 provides the responsive grid system and component library
2. **Brand Layer**: Custom CSS defines the floral business identity through colors, typography, and spacing
3. **Template Layer**: Updated Thymeleaf templates integrate Bootstrap components with custom styling
4. **Fragment Layer**: Reusable fragments ensure consistency across all pages

## Architecture

### Technology Stack

**CSS Framework:**
- Bootstrap 5.3.x (latest stable)
- Delivered via CDN for optimal performance
- Custom CSS overrides for brand-specific styling

**Template Engine:**
- Thymeleaf (existing)
- Thymeleaf Layout Dialect (existing)
- Enhanced with Bootstrap-specific attributes

**File Organization:**
```
src/main/resources/
├── static/
│   ├── css/
│   │   ├── bootstrap.min.css (CDN fallback)
│   │   ├── custom.css (brand styling)
│   │   ├── customer.css (customer-facing pages)
│   │   └── admin.css (admin dashboard)
│   ├── js/
│   │   ├── bootstrap.bundle.min.js (CDN fallback)
│   │   └── custom.js (custom interactions)
│   └── images/ (existing structure)
└── templates/
    ├── fragments/
    │   ├── header.html
    │   ├── footer.html
    │   ├── navbar.html
    │   └── sidebar.html
    ├── customer/ (customer pages)
    └── dashboard/ (admin pages)
```

### Design Principles

1. **Mobile-First**: Design for mobile screens first, then enhance for larger screens
2. **Progressive Enhancement**: Core functionality works without JavaScript, enhanced with JS
3. **Consistency**: Reusable components and fragments ensure uniform appearance
4. **Accessibility**: WCAG 2.1 Level AA compliance throughout
5. **Performance**: Optimized assets, lazy loading, and efficient CSS

## Components and Interfaces

### Bootstrap Integration Component

**Purpose**: Manages Bootstrap framework integration and configuration

**Implementation:**
- Include Bootstrap CSS via CDN with local fallback
- Include Bootstrap JavaScript bundle (includes Popper.js)
- Configure Bootstrap theme customization via CSS variables

**Template Integration:**
```html
<!-- In base layout template -->
<head>
    <!-- Bootstrap CSS from CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" 
          rel="stylesheet" 
          integrity="sha384-..." 
          crossorigin="anonymous">
    
    <!-- Custom CSS (loads after Bootstrap) -->
    <link rel="stylesheet" th:href="@{/css/custom.css}">
    <link rel="stylesheet" th:href="@{/css/customer.css}" th:if="${!isAdmin}">
    <link rel="stylesheet" th:href="@{/css/admin.css}" th:if="${isAdmin}">
</head>
<body>
    <!-- Content -->
    
    <!-- Bootstrap JS from CDN -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" 
            integrity="sha384-..." 
            crossorigin="anonymous"></script>
    
    <!-- Custom JS -->
    <script th:src="@{/js/custom.js}"></script>
</body>
```

### Custom Brand Styling Component

**Purpose**: Defines the visual identity for the floral business

**Color Palette:**
```css
:root {
    /* Primary Colors - Floral Theme */
    --primary-color: #E91E63;        /* Pink - main brand color */
    --primary-light: #F8BBD0;        /* Light pink */
    --primary-dark: #C2185B;         /* Dark pink */
    
    /* Secondary Colors */
    --secondary-color: #4CAF50;      /* Green - nature/freshness */
    --secondary-light: #A5D6A7;      /* Light green */
    --secondary-dark: #388E3C;       /* Dark green */
    
    /* Neutral Colors */
    --neutral-100: #FFFFFF;          /* White */
    --neutral-200: #F5F5F5;          /* Light gray */
    --neutral-300: #E0E0E0;          /* Gray */
    --neutral-700: #616161;          /* Dark gray */
    --neutral-900: #212121;          /* Almost black */
    
    /* Semantic Colors */
    --success-color: #4CAF50;
    --warning-color: #FF9800;
    --error-color: #F44336;
    --info-color: #2196F3;
    
    /* Typography */
    --font-primary: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    --font-heading: 'Georgia', serif;
    
    /* Spacing */
    --spacing-xs: 0.25rem;
    --spacing-sm: 0.5rem;
    --spacing-md: 1rem;
    --spacing-lg: 1.5rem;
    --spacing-xl: 2rem;
    --spacing-xxl: 3rem;
    
    /* Border Radius */
    --radius-sm: 0.25rem;
    --radius-md: 0.5rem;
    --radius-lg: 1rem;
    
    /* Shadows */
    --shadow-sm: 0 1px 3px rgba(0,0,0,0.12);
    --shadow-md: 0 4px 6px rgba(0,0,0,0.1);
    --shadow-lg: 0 10px 20px rgba(0,0,0,0.15);
}
```

**Typography System:**
```css
body {
    font-family: var(--font-primary);
    font-size: 16px;
    line-height: 1.6;
    color: var(--neutral-900);
}

h1, h2, h3, h4, h5, h6 {
    font-family: var(--font-heading);
    font-weight: 600;
    line-height: 1.3;
    margin-bottom: var(--spacing-md);
}

h1 { font-size: 2.5rem; }
h2 { font-size: 2rem; }
h3 { font-size: 1.75rem; }
h4 { font-size: 1.5rem; }
h5 { font-size: 1.25rem; }
h6 { font-size: 1rem; }
```

### Responsive Layout Component

**Purpose**: Implements responsive behavior across device sizes

**Breakpoint Strategy:**
- Mobile: 320px - 767px (single column, stacked elements)
- Tablet: 768px - 1023px (2-3 columns, optimized spacing)
- Desktop: 1024px+ (full multi-column layouts)

**Grid Implementation:**
```html
<!-- Product Grid Example -->
<div class="container">
    <div class="row g-4">
        <div class="col-12 col-sm-6 col-md-4 col-lg-3" 
             th:each="product : ${products}">
            <!-- Product card -->
        </div>
    </div>
</div>
```

**Responsive Utilities:**
```css
/* Mobile-first base styles */
.product-card {
    margin-bottom: var(--spacing-md);
}

/* Tablet adjustments */
@media (min-width: 768px) {
    .product-card {
        margin-bottom: var(--spacing-lg);
    }
}

/* Desktop adjustments */
@media (min-width: 1024px) {
    .product-card {
        margin-bottom: var(--spacing-xl);
    }
}
```

### Customer Pages Component

**Purpose**: Implements redesigned customer-facing pages

**Home Page Structure:**
```html
<div class="home-page">
    <!-- Hero Section -->
    <section class="hero bg-primary-light py-5">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-lg-6">
                    <h1 class="display-4">Beautiful Flowers for Every Occasion</h1>
                    <p class="lead">Fresh, handcrafted arrangements delivered to your door</p>
                    <a href="/products" class="btn btn-primary btn-lg">Shop Now</a>
                </div>
                <div class="col-lg-6">
                    <img src="/images/hero-flowers.jpg" alt="Beautiful flower arrangement" class="img-fluid rounded">
                </div>
            </div>
        </div>
    </section>
    
    <!-- Featured Products -->
    <section class="featured-products py-5">
        <div class="container">
            <h2 class="text-center mb-4">Featured Products</h2>
            <div class="row g-4">
                <!-- Product cards using Bootstrap grid -->
            </div>
        </div>
    </section>
    
    <!-- Categories -->
    <section class="categories py-5 bg-light">
        <div class="container">
            <h2 class="text-center mb-4">Shop by Category</h2>
            <div class="row g-4">
                <!-- Category cards -->
            </div>
        </div>
    </section>
</div>
```

**Product Card Component:**
```html
<div class="card product-card h-100 shadow-sm">
    <img th:src="@{'/images/products/' + ${product.image}}" 
         class="card-img-top" 
         th:alt="${product.name}">
    <div class="card-body d-flex flex-column">
        <h5 class="card-title" th:text="${product.name}">Product Name</h5>
        <p class="card-text text-muted small" th:text="${product.category.name}">Category</p>
        <div class="mt-auto">
            <p class="h5 text-primary mb-3" th:text="${#numbers.formatCurrency(product.price)}">Price</p>
            <a th:href="@{'/product/' + ${product.id}}" class="btn btn-primary w-100">View Details</a>
        </div>
    </div>
</div>
```

**Product Detail Page:**
```html
<div class="product-detail py-5">
    <div class="container">
        <div class="row">
            <div class="col-lg-6">
                <img th:src="@{'/images/products/' + ${product.image}}" 
                     class="img-fluid rounded shadow" 
                     th:alt="${product.name}">
            </div>
            <div class="col-lg-6">
                <h1 th:text="${product.name}">Product Name</h1>
                <p class="text-muted" th:text="${product.category.name}">Category</p>
                <h2 class="text-primary mb-4" th:text="${#numbers.formatCurrency(product.price)}">Price</h2>
                <p th:text="${product.description}">Description</p>
                
                <form th:action="@{/cart/add}" method="post" class="mt-4">
                    <input type="hidden" name="productId" th:value="${product.id}">
                    <div class="mb-3">
                        <label for="quantity" class="form-label">Quantity</label>
                        <input type="number" class="form-control" id="quantity" name="quantity" value="1" min="1">
                    </div>
                    <button type="submit" class="btn btn-primary btn-lg w-100">Add to Cart</button>
                </form>
            </div>
        </div>
    </div>
</div>
```

**Checkout Form:**
```html
<div class="checkout py-5">
    <div class="container">
        <div class="row">
            <div class="col-lg-8">
                <h2 class="mb-4">Checkout</h2>
                <form th:action="@{/order/create}" method="post" th:object="${orderForm}">
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="mb-0">Shipping Information</h5>
                        </div>
                        <div class="card-body">
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label for="fullName" class="form-label">Full Name</label>
                                    <input type="text" class="form-control" id="fullName" 
                                           th:field="*{fullName}" required>
                                    <div class="invalid-feedback" th:errors="*{fullName}"></div>
                                </div>
                                <div class="col-md-6">
                                    <label for="phone" class="form-label">Phone</label>
                                    <input type="tel" class="form-control" id="phone" 
                                           th:field="*{phone}" required>
                                    <div class="invalid-feedback" th:errors="*{phone}"></div>
                                </div>
                                <div class="col-12">
                                    <label for="address" class="form-label">Address</label>
                                    <textarea class="form-control" id="address" 
                                              th:field="*{address}" rows="3" required></textarea>
                                    <div class="invalid-feedback" th:errors="*{address}"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <button type="submit" class="btn btn-primary btn-lg w-100">Place Order</button>
                </form>
            </div>
            
            <div class="col-lg-4">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">Order Summary</h5>
                    </div>
                    <div class="card-body">
                        <!-- Cart items and total -->
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
```

### Admin Dashboard Component

**Purpose**: Implements redesigned admin interface

**Dashboard Layout:**
```html
<div class="admin-dashboard d-flex">
    <!-- Sidebar -->
    <nav class="sidebar bg-dark text-white" th:replace="~{fragments/sidebar :: sidebar}"></nav>
    
    <!-- Main Content -->
    <main class="main-content flex-grow-1">
        <!-- Top Navigation Bar -->
        <nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom">
            <div class="container-fluid">
                <button class="btn btn-link" id="sidebarToggle">
                    <i class="bi bi-list"></i>
                </button>
                <div class="ms-auto">
                    <!-- User menu -->
                </div>
            </div>
        </nav>
        
        <!-- Page Content -->
        <div class="container-fluid p-4">
            <div layout:fragment="content">
                <!-- Page-specific content -->
            </div>
        </div>
    </main>
</div>
```

**Sidebar Navigation:**
```html
<div class="sidebar" th:fragment="sidebar">
    <div class="sidebar-header p-3">
        <h4>AccessMotor Admin</h4>
    </div>
    <ul class="nav flex-column">
        <li class="nav-item">
            <a class="nav-link" th:href="@{/dashboard}" 
               th:classappend="${#request.requestURI == '/dashboard' ? 'active' : ''}">
                <i class="bi bi-speedometer2"></i> Dashboard
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" th:href="@{/dashboard/products}"
               th:classappend="${#strings.contains(#request.requestURI, '/products') ? 'active' : ''}">
                <i class="bi bi-box"></i> Products
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" th:href="@{/dashboard/categories}">
                <i class="bi bi-tags"></i> Categories
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" th:href="@{/dashboard/orders}">
                <i class="bi bi-cart"></i> Orders
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" th:href="@{/dashboard/accounts}">
                <i class="bi bi-people"></i> Accounts
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" th:href="@{/dashboard/discounts}">
                <i class="bi bi-percent"></i> Discounts
            </a>
        </li>
    </ul>
</div>
```

**Data Table Component:**
```html
<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h5 class="mb-0">Products</h5>
        <a th:href="@{/dashboard/products/create}" class="btn btn-primary">
            <i class="bi bi-plus-circle"></i> Add Product
        </a>
    </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Image</th>
                        <th>Name</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr th:each="product : ${products}">
                        <td th:text="${product.id}">1</td>
                        <td>
                            <img th:src="@{'/images/products/' + ${product.image}}" 
                                 class="img-thumbnail" 
                                 style="width: 50px; height: 50px; object-fit: cover;">
                        </td>
                        <td th:text="${product.name}">Product Name</td>
                        <td th:text="${product.category.name}">Category</td>
                        <td th:text="${#numbers.formatCurrency(product.price)}">Price</td>
                        <td th:text="${product.quantity}">Stock</td>
                        <td>
                            <a th:href="@{'/dashboard/products/edit/' + ${product.id}}" 
                               class="btn btn-sm btn-outline-primary">
                                <i class="bi bi-pencil"></i>
                            </a>
                            <button type="button" class="btn btn-sm btn-outline-danger"
                                    data-bs-toggle="modal" 
                                    th:data-bs-target="'#deleteModal' + ${product.id}">
                                <i class="bi bi-trash"></i>
                            </button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        
        <!-- Pagination -->
        <nav th:if="${products.totalPages > 1}">
            <ul class="pagination justify-content-center">
                <li class="page-item" th:classappend="${products.first ? 'disabled' : ''}">
                    <a class="page-link" th:href="@{/dashboard/products(page=${products.number - 1})}">Previous</a>
                </li>
                <li class="page-item" th:each="i : ${#numbers.sequence(0, products.totalPages - 1)}"
                    th:classappend="${i == products.number ? 'active' : ''}">
                    <a class="page-link" th:href="@{/dashboard/products(page=${i})}" th:text="${i + 1}">1</a>
                </li>
                <li class="page-item" th:classappend="${products.last ? 'disabled' : ''}">
                    <a class="page-link" th:href="@{/dashboard/products(page=${products.number + 1})}">Next</a>
                </li>
            </ul>
        </nav>
    </div>
</div>
```

**Admin Form Component:**
```html
<div class="card">
    <div class="card-header">
        <h5 class="mb-0">Edit Product</h5>
    </div>
    <div class="card-body">
        <form th:action="@{/dashboard/products/save}" method="post" 
              th:object="${product}" enctype="multipart/form-data">
            <input type="hidden" th:field="*{id}">
            
            <div class="row g-3">
                <div class="col-md-6">
                    <label for="name" class="form-label">Product Name</label>
                    <input type="text" class="form-control" id="name" 
                           th:field="*{name}" 
                           th:classappend="${#fields.hasErrors('name') ? 'is-invalid' : ''}" 
                           required>
                    <div class="invalid-feedback" th:errors="*{name}"></div>
                </div>
                
                <div class="col-md-6">
                    <label for="category" class="form-label">Category</label>
                    <select class="form-select" id="category" 
                            th:field="*{category.id}" 
                            required>
                        <option value="">Select Category</option>
                        <option th:each="cat : ${categories}" 
                                th:value="${cat.id}" 
                                th:text="${cat.name}">Category</option>
                    </select>
                    <div class="invalid-feedback" th:errors="*{category}"></div>
                </div>
                
                <div class="col-md-6">
                    <label for="price" class="form-label">Price</label>
                    <input type="number" class="form-control" id="price" 
                           th:field="*{price}" 
                           step="0.01" 
                           min="0" 
                           required>
                    <div class="invalid-feedback" th:errors="*{price}"></div>
                </div>
                
                <div class="col-md-6">
                    <label for="quantity" class="form-label">Stock Quantity</label>
                    <input type="number" class="form-control" id="quantity" 
                           th:field="*{quantity}" 
                           min="0" 
                           required>
                    <div class="invalid-feedback" th:errors="*{quantity}"></div>
                </div>
                
                <div class="col-12">
                    <label for="description" class="form-label">Description</label>
                    <textarea class="form-control" id="description" 
                              th:field="*{description}" 
                              rows="4"></textarea>
                </div>
                
                <div class="col-12">
                    <label for="image" class="form-label">Product Image</label>
                    <input type="file" class="form-control" id="image" 
                           name="imageFile" 
                           accept="image/*">
                    <small class="form-text text-muted">Current image: <span th:text="*{image}"></span></small>
                </div>
            </div>
            
            <div class="mt-4">
                <button type="submit" class="btn btn-primary">Save Product</button>
                <a th:href="@{/dashboard/products}" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>
```

### Thymeleaf Fragment Component

**Purpose**: Provides reusable template fragments

**Header Fragment:**
```html
<header th:fragment="header" class="header">
    <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
        <div class="container">
            <a class="navbar-brand" th:href="@{/}">
                <img src="/images/logo.png" alt="AccessMotor" height="40">
            </a>
            
            <button class="navbar-toggler" type="button" 
                    data-bs-toggle="collapse" 
                    data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" th:href="@{/}">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" th:href="@{/products}">Products</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" th:href="@{/about}">About</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" th:href="@{/contact}">Contact</a>
                    </li>
                    <li class="nav-item" sec:authorize="isAuthenticated()">
                        <a class="nav-link" th:href="@{/profile}">
                            <i class="bi bi-person"></i> Profile
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link position-relative" th:href="@{/cart}">
                            <i class="bi bi-cart"></i>
                            <span class="badge bg-primary position-absolute top-0 start-100 translate-middle"
                                  th:if="${cartItemCount > 0}"
                                  th:text="${cartItemCount}">0</span>
                        </a>
                    </li>
                    <li class="nav-item" sec:authorize="!isAuthenticated()">
                        <a class="nav-link" th:href="@{/login}">Login</a>
                    </li>
                    <li class="nav-item" sec:authorize="isAuthenticated()">
                        <a class="nav-link" th:href="@{/logout}">Logout</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</header>
```

**Footer Fragment:**
```html
<footer th:fragment="footer" class="footer bg-dark text-white mt-5">
    <div class="container py-5">
        <div class="row">
            <div class="col-md-4 mb-4">
                <h5>About AccessMotor</h5>
                <p>Your trusted source for beautiful, fresh flowers and floral arrangements for every occasion.</p>
            </div>
            <div class="col-md-4 mb-4">
                <h5>Quick Links</h5>
                <ul class="list-unstyled">
                    <li><a th:href="@{/}" class="text-white-50">Home</a></li>
                    <li><a th:href="@{/products}" class="text-white-50">Products</a></li>
                    <li><a th:href="@{/about}" class="text-white-50">About Us</a></li>
                    <li><a th:href="@{/contact}" class="text-white-50">Contact</a></li>
                </ul>
            </div>
            <div class="col-md-4 mb-4">
                <h5>Contact Us</h5>
                <ul class="list-unstyled">
                    <li><i class="bi bi-telephone"></i> +84 123 456 789</li>
                    <li><i class="bi bi-envelope"></i> info@accessmotor.com</li>
                    <li><i class="bi bi-geo-alt"></i> Ho Chi Minh City, Vietnam</li>
                </ul>
            </div>
        </div>
        <hr class="bg-white">
        <div class="text-center">
            <p class="mb-0">&copy; 2024 AccessMotor. All rights reserved.</p>
        </div>
    </div>
</footer>
```

## Data Models

No new data models are required for this feature. The theme redesign works with existing entities:
- Product
- Category
- Order
- Account
- Discount

The redesign focuses on presentation layer changes (templates and CSS) without modifying the data layer.


## Correctness Properties

*A property is a characteristic or behavior that should hold true across all valid executions of a system—essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

### Property Reflection

After analyzing all acceptance criteria, I identified several areas where properties can be consolidated:

**Responsive Layout Properties (3.1, 3.2, 3.3)**: These three criteria all test responsive behavior at different breakpoints. Rather than three separate properties, we can combine them into a single comprehensive property that tests layout adaptation across all breakpoints.

**Form Accessibility Properties (7.3, 4.4)**: Both criteria verify that form fields have labels and validation. These can be combined into a single property about form accessibility.

**Functionality Preservation Properties (10.1, 10.2, 10.3, 10.5)**: These all verify that existing functionality continues to work. They can be consolidated into fewer, more comprehensive properties.

**Bootstrap Component Usage (1.4, 1.5)**: Both verify Bootstrap classes are present in HTML. These can be combined into a single property about Bootstrap integration.

After reflection, the following properties provide comprehensive coverage without redundancy:

### Property 1: Bootstrap Integration

*For any* rendered page, the HTML output should include Bootstrap 5.3+ CSS and JavaScript files from CDN sources.

**Validates: Requirements 1.1, 1.2, 9.2**

### Property 2: Bootstrap Grid System Usage

*For any* page template, the rendered HTML should contain Bootstrap grid classes (container, row, col-*) for layout structure.

**Validates: Requirements 1.4**

### Property 3: Template Compatibility

*For any* existing Thymeleaf template, rendering with the new theme should complete without errors and produce valid HTML.

**Validates: Requirements 1.3, 10.5**

### Property 4: Responsive Layout Adaptation

*For any* page rendered at different viewport widths (mobile: 320-767px, tablet: 768-1023px, desktop: 1024px+), the layout should adapt appropriately without horizontal scrolling, with mobile showing single-column layouts and larger screens showing multi-column layouts.

**Validates: Requirements 3.1, 3.2, 3.3, 3.4**

### Property 5: Product Card Structure

*For any* product displayed in the catalog, the rendered card should contain an image element, a title element, and a price element with appropriate Bootstrap card classes.

**Validates: Requirements 4.2**

### Property 6: Form Accessibility

*For any* form input element, it should have an associated label element (either wrapping it or linked via for/id attributes) and display validation feedback when validation fails.

**Validates: Requirements 4.4, 7.3**

### Property 7: Data Table Structure

*For any* admin data table, the rendered HTML should include Bootstrap table classes and pagination elements when the dataset has multiple pages.

**Validates: Requirements 5.2**

### Property 8: Admin Form Consistency

*For any* admin form, all form controls should use consistent Bootstrap form classes (form-control, form-select, form-label) and display validation feedback.

**Validates: Requirements 5.3**

### Property 9: CRUD Action Feedback

*For any* CRUD operation (create, update, delete), the system should display visual feedback (success or error message) after the action completes.

**Validates: Requirements 5.5**

### Property 10: Fragment Consistency

*For any* Thymeleaf fragment (header, footer, navbar, sidebar), the rendered HTML should use consistent Bootstrap class naming patterns and custom CSS classes.

**Validates: Requirements 6.5**

### Property 11: Semantic HTML Structure

*For any* page, the HTML should use semantic HTML5 elements (header, nav, main, section, article, footer) for document structure rather than generic div elements for major page sections.

**Validates: Requirements 7.1**

### Property 12: Image Accessibility

*For any* img element in the rendered HTML, it should have a non-empty alt attribute providing descriptive text.

**Validates: Requirements 7.2**

### Property 13: Color Contrast Compliance

*For any* text element with a background color, the color contrast ratio between text and background should meet or exceed 4.5:1 for normal text and 3:1 for large text.

**Validates: Requirements 7.4**

### Property 14: Keyboard Navigation Support

*For any* interactive element (links, buttons, form controls), it should be keyboard focusable and display visible focus styles when focused.

**Validates: Requirements 7.5**

### Property 15: Vietnamese Text Display

*For any* page containing Vietnamese text with diacritics, the text should render correctly without character encoding issues or missing diacritical marks.

**Validates: Requirements 8.4**

### Property 16: Responsive Image Sizing

*For any* image displayed on a page, it should have CSS properties (max-width, height) that prevent it from exceeding its container width at any viewport size.

**Validates: Requirements 9.3**

### Property 17: Form Submission Preservation

*For any* form in the application, submitting the form with valid data should successfully process the submission and produce the expected result (same behavior as before theme update).

**Validates: Requirements 10.1**

### Property 18: Navigation Functionality Preservation

*For any* navigation link in the application, clicking the link should navigate to the correct destination URL (same behavior as before theme update).

**Validates: Requirements 10.2**

### Property 19: JavaScript Functionality Preservation

*For any* existing JavaScript feature (cart updates, form validation, modals), the feature should function correctly with the new theme (same behavior as before theme update).

**Validates: Requirements 10.3**

## Error Handling

### CSS Loading Failures

**Scenario**: CDN fails to load Bootstrap CSS

**Handling**:
- Include local fallback Bootstrap files in `/static/css/`
- Use JavaScript to detect CDN failure and load local fallback
- Ensure custom CSS still applies even if Bootstrap fails

**Implementation**:
```html
<link rel="stylesheet" 
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
      integrity="sha384-..."
      crossorigin="anonymous"
      onerror="this.onerror=null;this.href='/css/bootstrap.min.css';">
```

### JavaScript Loading Failures

**Scenario**: CDN fails to load Bootstrap JavaScript

**Handling**:
- Include local fallback Bootstrap JS in `/static/js/`
- Detect CDN failure and load local fallback
- Ensure core functionality works without JavaScript (progressive enhancement)

**Implementation**:
```html
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-..."
        crossorigin="anonymous"
        onerror="loadLocalBootstrap()"></script>
<script>
function loadLocalBootstrap() {
    var script = document.createElement('script');
    script.src = '/js/bootstrap.bundle.min.js';
    document.head.appendChild(script);
}
</script>
```

### Image Loading Failures

**Scenario**: Product image fails to load

**Handling**:
- Display placeholder image
- Maintain layout integrity
- Log error for monitoring

**Implementation**:
```html
<img th:src="@{'/images/products/' + ${product.image}}" 
     class="card-img-top" 
     th:alt="${product.name}"
     onerror="this.src='/images/placeholder.jpg';">
```

### Responsive Layout Issues

**Scenario**: Content overflows on small screens

**Handling**:
- Use Bootstrap's responsive utilities
- Apply `overflow-x: hidden` to body
- Test all breakpoints during development
- Use `img { max-width: 100%; height: auto; }` globally

### Browser Compatibility Issues

**Scenario**: Older browsers don't support CSS features

**Handling**:
- Use Bootstrap's built-in browser compatibility
- Provide fallbacks for CSS custom properties
- Test on target browsers (Chrome, Firefox, Safari, Edge)
- Use autoprefixer for vendor prefixes

### Accessibility Violations

**Scenario**: Automated accessibility tests fail

**Handling**:
- Run accessibility audits during development (axe, Lighthouse)
- Fix violations before deployment
- Ensure all interactive elements are keyboard accessible
- Maintain proper heading hierarchy
- Provide skip navigation links

## Testing Strategy

### Dual Testing Approach

The theme redesign requires both unit tests and property-based tests to ensure comprehensive coverage:

**Unit Tests**: Focus on specific examples, edge cases, and integration points
- Test specific page renders (home page, product detail, checkout)
- Test fragment rendering (header, footer, navbar, sidebar)
- Test responsive behavior at specific breakpoints
- Test error handling scenarios (image load failures, CDN failures)
- Test accessibility features (keyboard navigation, screen reader compatibility)

**Property-Based Tests**: Verify universal properties across all inputs
- Test that all pages include Bootstrap resources
- Test that all forms have proper labels and validation
- Test that all images have alt text
- Test that all interactive elements are keyboard accessible
- Test that all pages have semantic HTML structure
- Test color contrast ratios across all text elements
- Test responsive behavior across random viewport widths

### Property-Based Testing Configuration

**Testing Library**: Use a Java property-based testing library such as:
- **jqwik** (recommended for Java 17+)
- **QuickTheories**
- **junit-quickcheck**

**Configuration**:
- Minimum 100 iterations per property test
- Each test tagged with feature name and property number
- Tag format: `@Tag("Feature: theme-redesign, Property {N}: {property_text}")`

**Example Property Test Structure**:
```java
@Property
@Tag("Feature: theme-redesign, Property 12: Image Accessibility")
void allImagesShouldHaveAltText(@ForAll("pageUrls") String pageUrl) {
    // Render page
    String html = renderPage(pageUrl);
    
    // Parse HTML
    Document doc = Jsoup.parse(html);
    
    // Find all images
    Elements images = doc.select("img");
    
    // Verify all have alt attributes
    for (Element img : images) {
        assertThat(img.hasAttr("alt"))
            .as("Image should have alt attribute: " + img)
            .isTrue();
        assertThat(img.attr("alt"))
            .as("Alt attribute should not be empty")
            .isNotEmpty();
    }
}

@Provide
Arbitrary<String> pageUrls() {
    return Arbitraries.of(
        "/", "/products", "/product/1", "/cart", 
        "/checkout", "/about", "/contact", "/profile",
        "/dashboard", "/dashboard/products", "/dashboard/orders"
    );
}
```

### Testing Phases

**Phase 1: Component Testing**
- Test individual template fragments in isolation
- Test CSS class application
- Test responsive utilities
- Test Bootstrap component integration

**Phase 2: Integration Testing**
- Test complete page renders
- Test navigation flows
- Test form submissions
- Test CRUD operations with new theme

**Phase 3: Visual Regression Testing**
- Capture screenshots of pages before and after theme update
- Compare layouts and styling
- Verify no unintended visual changes
- Test across different browsers

**Phase 4: Accessibility Testing**
- Run automated accessibility audits (axe-core, Lighthouse)
- Test keyboard navigation manually
- Test with screen readers (NVDA, JAWS)
- Verify WCAG 2.1 Level AA compliance

**Phase 5: Performance Testing**
- Measure page load times
- Analyze CSS and JavaScript bundle sizes
- Test on slow network connections
- Verify CDN delivery works correctly

**Phase 6: Cross-Browser Testing**
- Test on Chrome, Firefox, Safari, Edge
- Test on mobile browsers (iOS Safari, Chrome Mobile)
- Verify consistent appearance and functionality
- Fix browser-specific issues

### Test Coverage Goals

- **Template Coverage**: 100% of templates rendered and tested
- **Fragment Coverage**: 100% of fragments tested in isolation and integration
- **Responsive Coverage**: All breakpoints tested (mobile, tablet, desktop)
- **Accessibility Coverage**: All WCAG 2.1 Level AA criteria verified
- **Functionality Coverage**: All existing features tested for preservation
- **Property Coverage**: All 19 correctness properties implemented as property-based tests

### Continuous Testing

- Run unit tests on every commit
- Run property-based tests on every pull request
- Run accessibility audits on every deployment
- Run visual regression tests on staging environment
- Monitor performance metrics in production
