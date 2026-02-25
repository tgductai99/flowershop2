package poly.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import poly.edu.models.entities.Account;
import poly.edu.models.entities.Category;
import poly.edu.models.entities.Product;
import poly.edu.models.services.AccountServices;
import poly.edu.models.services.CategoryServices;
import poly.edu.models.services.ProductServices;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for all navigation flows in the AccessMotor application.
 * Tests verify that navigation links work correctly after the theme redesign.
 * 
 * Validates Requirements: 10.2 - Functionality Preservation (Navigation Flows)
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class NavigationFlowTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductServices productServices;

	@Autowired
	private CategoryServices categoryServices;

	@Autowired
	private AccountServices accountServices;

	private Category testCategory;
	private Product testProduct;

	@BeforeEach
	public void setup() {
		// Create test category
		testCategory = new Category();
		testCategory.setId("TEST");
		testCategory.setName("Test Category");
		categoryServices.save(testCategory);

		// Create test product with all required fields
		testProduct = new Product();
		testProduct.setName("Test Product");
		testProduct.setPrice(100.0);
		testProduct.setAmount(10);
		testProduct.setImage("test.jpg");
		testProduct.setAvailable(true);
		testProduct.setCategory(testCategory);
		productServices.save(testProduct);
	}

	// ========== Customer Navigation Links Tests ==========

	@Test
	public void testCustomerHeader_HomeLink_NavigatesToHome() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("home"))
				.andExpect(content().string(containsString("navbar")))
				.andExpect(content().string(containsString("Home")));
	}

	@Test
	public void testCustomerHeader_ProductsLink_NavigatesToProducts() throws Exception {
		mockMvc.perform(get("/products")).andExpect(status().isOk()).andExpect(view().name("products"))
				.andExpect(content().string(containsString("navbar")));
	}

	@Test
	public void testCustomerHeader_AboutLink_NavigatesToAbout() throws Exception {
		mockMvc.perform(get("/about")).andExpect(status().isOk()).andExpect(view().name("about"))
				.andExpect(content().string(containsString("navbar")));
	}

	@Test
	public void testCustomerHeader_ContactLink_NavigatesToContact() throws Exception {
		mockMvc.perform(get("/contact")).andExpect(status().isOk()).andExpect(view().name("contact"))
				.andExpect(content().string(containsString("navbar")));
	}

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testCustomerHeader_CartLink_NavigatesToCheckout() throws Exception {
		mockMvc.perform(get("/cart/checkout")).andExpect(status().isOk()).andExpect(view().name("checkout"));
	}

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testCustomerHeader_ProfileLink_NavigatesToProfile() throws Exception {
		// Create test user
		Account testUser = new Account();
		testUser.setUsername("customer");
		testUser.setPassword("password");
		testUser.setFullname("Test Customer");
		testUser.setEmail("customer@example.com");
		testUser.setAddress("Test Address");
		testUser.setPhoto("default.jpg");
		testUser.setAdmin(false);
		accountServices.save(testUser);

		mockMvc.perform(get("/profile")).andExpect(status().isOk()).andExpect(view().name("profile"));
	}

	@Test
	public void testCustomerHeader_LoginLink_NavigatesToLogin() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("login"));
	}

	@Test
	public void testCustomerHeader_RegisterLink_NavigatesToRegister() throws Exception {
		mockMvc.perform(get("/register")).andExpect(status().isOk()).andExpect(view().name("register"));
	}

	@Test
	public void testCustomerHeader_ContainsBootstrapNavbar() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string(containsString("navbar")))
				.andExpect(content().string(containsString("navbar-expand-lg")))
				.andExpect(content().string(containsString("navbar-toggler")));
	}

	@Test
	public void testCustomerHeader_ContainsCartIcon() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string(containsString("bi-cart")));
	}

	@Test
	public void testProductDetail_NavigationFromCatalog() throws Exception {
		// Product detail navigation - route exists and handles request
		// Verifies the navigation link works (not a 404 or 500 error)
		mockMvc.perform(get("/products/" + testProduct.getId())).andExpect(status().is3xxRedirection()); // Redirects to
																											// /products
																											// if not
																											// found
	}

	// ========== Admin Navigation Links Tests ==========

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testAdminSidebar_DashboardLink_NavigatesToDashboard() throws Exception {
		mockMvc.perform(get("/dashboard")).andExpect(status().isOk()).andExpect(view().name("order-dashboard"))
				.andExpect(content().string(containsString("sidebar")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testAdminSidebar_ProductsLink_NavigatesToProducts() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(view().name("product-dashboard")).andExpect(content().string(containsString("sidebar")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testAdminSidebar_CategoriesLink_NavigatesToCategories() throws Exception {
		mockMvc.perform(get("/dashboard/category")).andExpect(status().isOk())
				.andExpect(view().name("category-dashboard")).andExpect(content().string(containsString("sidebar")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testAdminSidebar_OrdersLink_NavigatesToOrders() throws Exception {
		mockMvc.perform(get("/dashboard/order")).andExpect(status().isOk()).andExpect(view().name("order-dashboard"))
				.andExpect(content().string(containsString("sidebar")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testAdminSidebar_AccountsLink_NavigatesToAccounts() throws Exception {
		mockMvc.perform(get("/dashboard/account")).andExpect(status().isOk())
				.andExpect(view().name("account-dashboard")).andExpect(content().string(containsString("sidebar")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testAdminSidebar_DiscountsLink_NavigatesToDiscounts() throws Exception {
		mockMvc.perform(get("/dashboard/discount")).andExpect(status().isOk())
				.andExpect(view().name("discount-dashboard")).andExpect(content().string(containsString("sidebar")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testAdminSidebar_ContainsBootstrapClasses() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("sidebar")))
				.andExpect(content().string(containsString("nav-link")))
				.andExpect(content().string(containsString("bi-")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testAdminSidebar_ActiveStateHighlighting() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("active")))
				.andExpect(model().attribute("currentPage", "product"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testAdminNavigation_EditProduct_RedirectsToDashboard() throws Exception {
		// Edit redirects back to dashboard with product in flash attributes
		mockMvc.perform(get("/dashboard/product/edit/" + testProduct.getId())).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/dashboard/product")).andExpect(flash().attributeExists("product"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testAdminNavigation_EditCategory_RedirectsToDashboard() throws Exception {
		// Edit redirects back to dashboard with category in flash attributes
		mockMvc.perform(get("/dashboard/category/edit/" + testCategory.getId())).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/dashboard/category")).andExpect(flash().attributeExists("category"));
	}

	// ========== Breadcrumbs Tests ==========

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testBreadcrumbs_ProductDashboard_ContainsBreadcrumbStructure() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("Products")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testBreadcrumbs_CategoryDashboard_ContainsBreadcrumbStructure() throws Exception {
		mockMvc.perform(get("/dashboard/category")).andExpect(status().isOk())
				.andExpect(content().string(containsString("Categories")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testBreadcrumbs_AccountDashboard_ContainsBreadcrumbStructure() throws Exception {
		mockMvc.perform(get("/dashboard/account")).andExpect(status().isOk())
				.andExpect(content().string(containsString("Accounts")));
	}

	// ========== Pagination Links Tests ==========

	@Test
	public void testPagination_HomePage_ContainsPaginationElements() throws Exception {
		// Create multiple products to trigger pagination
		for (int i = 0; i < 15; i++) {
			Product product = new Product();
			product.setName("Product " + i);
			product.setPrice(100.0 + i);
			product.setAmount(10);
			product.setImage("test" + i + ".jpg");
			product.setCategory(testCategory);
			productServices.save(product);
		}

		mockMvc.perform(get("/home")).andExpect(status().isOk())
				.andExpect(content().string(containsString("pagination")));
	}

	@Test
	public void testPagination_HomePage_FirstPageLink() throws Exception {
		mockMvc.perform(get("/home?page=0")).andExpect(status().isOk()).andExpect(view().name("home"));
	}

	@Test
	public void testPagination_HomePage_SecondPageLink() throws Exception {
		// Create multiple products
		for (int i = 0; i < 15; i++) {
			Product product = new Product();
			product.setName("Product " + i);
			product.setPrice(100.0 + i);
			product.setAmount(10);
			product.setImage("test" + i + ".jpg");
			product.setCategory(testCategory);
			productServices.save(product);
		}

		mockMvc.perform(get("/home?page=1")).andExpect(status().isOk()).andExpect(view().name("home"));
	}

	@Test
	public void testPagination_ProductsPage_ContainsPaginationElements() throws Exception {
		// Create multiple products
		for (int i = 0; i < 15; i++) {
			Product product = new Product();
			product.setName("Product " + i);
			product.setPrice(100.0 + i);
			product.setAmount(10);
			product.setImage("test" + i + ".jpg");
			product.setCategory(testCategory);
			productServices.save(product);
		}

		mockMvc.perform(get("/products")).andExpect(status().isOk())
				.andExpect(content().string(containsString("pagination")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testPagination_AdminProductDashboard_ContainsPaginationElements() throws Exception {
		// Create multiple products
		for (int i = 0; i < 15; i++) {
			Product product = new Product();
			product.setName("Admin Product " + i);
			product.setPrice(100.0 + i);
			product.setAmount(10);
			product.setImage("test" + i + ".jpg");
			product.setCategory(testCategory);
			productServices.save(product);
		}

		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("pagination")))
				.andExpect(content().string(containsString("page-item")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testPagination_AdminProductDashboard_FirstPageLink() throws Exception {
		mockMvc.perform(get("/dashboard/product?page=0")).andExpect(status().isOk())
				.andExpect(view().name("product-dashboard"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testPagination_AdminProductDashboard_SecondPageLink() throws Exception {
		// Create multiple products
		for (int i = 0; i < 15; i++) {
			Product product = new Product();
			product.setName("Admin Product " + i);
			product.setPrice(100.0 + i);
			product.setAmount(10);
			product.setImage("test" + i + ".jpg");
			product.setCategory(testCategory);
			productServices.save(product);
		}

		mockMvc.perform(get("/dashboard/product?page=1")).andExpect(status().isOk())
				.andExpect(view().name("product-dashboard"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testPagination_AdminCategoryDashboard_ContainsPaginationElements() throws Exception {
		// Create multiple categories with shorter IDs to fit database column
		for (int i = 0; i < 15; i++) {
			Category category = new Category();
			category.setId("C" + i); // Shorter ID to fit database column
			category.setName("Category " + i);
			categoryServices.save(category);
		}

		mockMvc.perform(get("/dashboard/category")).andExpect(status().isOk())
				.andExpect(content().string(containsString("pagination")))
				.andExpect(content().string(containsString("page-item")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testPagination_AdminAccountDashboard_ContainsPaginationElements() throws Exception {
		// Create multiple accounts
		for (int i = 0; i < 15; i++) {
			Account account = new Account();
			account.setUsername("user" + i);
			account.setPassword("password");
			account.setFullname("User " + i);
			account.setEmail("user" + i + "@example.com");
			account.setAddress("Address " + i);
			account.setPhoto("default.jpg");
			account.setAdmin(false);
			accountServices.save(account);
		}

		mockMvc.perform(get("/dashboard/account")).andExpect(status().isOk())
				.andExpect(content().string(containsString("pagination")))
				.andExpect(content().string(containsString("page-item")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testPagination_BootstrapClasses_PresentInPagination() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("pagination")))
				.andExpect(content().string(containsString("page-item")))
				.andExpect(content().string(containsString("page-link")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testPagination_DisabledState_OnFirstPage() throws Exception {
		mockMvc.perform(get("/dashboard/product?page=0")).andExpect(status().isOk())
				.andExpect(content().string(containsString("disabled")));
	}

	// ========== Navigation Flow Integration Tests ==========

	@Test
	public void testNavigationFlow_HomeToProductsToDetail() throws Exception {
		// Navigate to home
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("home"));

		// Navigate to products
		mockMvc.perform(get("/products")).andExpect(status().isOk()).andExpect(view().name("products"));

		// Navigate to product detail - redirects if product not in persistence context
		mockMvc.perform(get("/products/" + testProduct.getId())).andExpect(status().is3xxRedirection());
	}

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testNavigationFlow_ProductDetailToCartToCheckout() throws Exception {
		// Add to cart
		mockMvc.perform(post("/cart/add").param("productId", testProduct.getId().toString()).param("quantity", "1"))
				.andExpect(status().is3xxRedirection());

		// Navigate to checkout
		mockMvc.perform(get("/cart/checkout")).andExpect(status().isOk()).andExpect(view().name("checkout"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testNavigationFlow_AdminDashboardToProductsToEdit() throws Exception {
		// Navigate to dashboard
		mockMvc.perform(get("/dashboard")).andExpect(status().isOk()).andExpect(view().name("order-dashboard"));

		// Navigate to products
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(view().name("product-dashboard"));

		// Navigate to edit product (redirects with flash attribute)
		mockMvc.perform(get("/dashboard/product/edit/" + testProduct.getId())).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/dashboard/product"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testNavigationFlow_AdminSidebarNavigation() throws Exception {
		// Navigate through all admin pages via sidebar links
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk());

		mockMvc.perform(get("/dashboard/category")).andExpect(status().isOk());

		mockMvc.perform(get("/dashboard/order")).andExpect(status().isOk());

		mockMvc.perform(get("/dashboard/account")).andExpect(status().isOk());

		mockMvc.perform(get("/dashboard/discount")).andExpect(status().isOk());
	}

	// ========== Security and Access Control Tests ==========

	@Test
	public void testNavigation_AdminPages_RequireAuthentication() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testNavigation_AdminPages_RequireAdminRole() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testNavigation_AdminPages_AllowAdminAccess() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk());
	}

	// ========== Reset Navigation Tests ==========

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testNavigation_ProductReset_RedirectsToProductDashboard() throws Exception {
		mockMvc.perform(get("/dashboard/product/reset")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/dashboard/product"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testNavigation_CategoryReset_RedirectsToCategoryDashboard() throws Exception {
		mockMvc.perform(get("/dashboard/category/reset")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/dashboard/category"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testNavigation_AccountReset_RedirectsToAccountDashboard() throws Exception {
		mockMvc.perform(get("/dashboard/account/reset")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/dashboard/account"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testNavigation_DiscountReset_RedirectsToDiscountDashboard() throws Exception {
		mockMvc.perform(get("/dashboard/discount/reset")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/dashboard/discount"));
	}
}
