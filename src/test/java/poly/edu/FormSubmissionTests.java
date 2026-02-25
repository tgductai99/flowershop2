package poly.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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

/**
 * Integration tests for all form submissions in the AccessMotor application.
 * Tests verify that forms continue to work correctly after the theme redesign.
 * 
 * Note: CSRF is disabled in SecurityConfig, so no CSRF tokens are needed in
 * tests.
 * 
 * Validates Requirements: 10.1 - Functionality Preservation
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FormSubmissionTests {

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

		// Create test product
		testProduct = new Product();
		testProduct.setName("Test Product");
		testProduct.setPrice(100.0);
		testProduct.setAmount(10);
		testProduct.setImage("test.jpg");
		testProduct.setCategory(testCategory);
		productServices.save(testProduct);
	}

	// ========== Product Creation Form Tests ==========

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testProductCreationForm_ValidData_Success() throws Exception {
		MockMultipartFile imageFile = new MockMultipartFile("imageFile", "product.jpg", MediaType.IMAGE_JPEG_VALUE,
				"test image content".getBytes());

		mockMvc.perform(multipart("/dashboard/product/save").file(imageFile).param("name", "New Product")
				.param("price", "150.0").param("amount", "20").param("category.id", testCategory.getId()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/dashboard/product"))
				.andExpect(flash().attribute("message", "saveTrue"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testProductCreationForm_WithoutImage_Success() throws Exception {
		MockMultipartFile emptyFile = new MockMultipartFile("imageFile", "", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

		mockMvc.perform(multipart("/dashboard/product/save").file(emptyFile).param("name", "Product Without Image")
				.param("price", "200.0").param("amount", "5").param("category.id", testCategory.getId()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/dashboard/product"));
	}

	// ========== Product Edit Form Tests ==========

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testProductEditForm_ValidData_Success() throws Exception {
		MockMultipartFile emptyFile = new MockMultipartFile("imageFile", "", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

		mockMvc.perform(multipart("/dashboard/product/save").file(emptyFile).param("id", testProduct.getId().toString())
				.param("name", "Updated Product").param("price", "250.0").param("amount", "15")
				.param("category.id", testCategory.getId()).param("image", testProduct.getImage()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/dashboard/product"))
				.andExpect(flash().attribute("message", "saveTrue"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testProductEditForm_WithNewImage_Success() throws Exception {
		MockMultipartFile newImageFile = new MockMultipartFile("imageFile", "updated.jpg", MediaType.IMAGE_JPEG_VALUE,
				"updated image content".getBytes());

		mockMvc.perform(multipart("/dashboard/product/save").file(newImageFile)
				.param("id", testProduct.getId().toString()).param("name", "Updated Product with Image")
				.param("price", "300.0").param("amount", "25").param("category.id", testCategory.getId()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/dashboard/product"));
	}

	// ========== Checkout Form Tests ==========

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testCheckoutForm_AddToCart_Success() throws Exception {
		mockMvc.perform(post("/cart/add").param("productId", testProduct.getId().toString()).param("quantity", "2"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/products"));
	}

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testCheckoutForm_BuyNow_Success() throws Exception {
		mockMvc.perform(post("/cart/buy").param("productId", testProduct.getId().toString()).param("quantity", "1"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/cart/checkout"));
	}

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testCheckoutForm_RemoveItem_Success() throws Exception {
		mockMvc.perform(get("/cart/remove").param("productId", testProduct.getId().toString()))
				.andExpect(status().isOk()).andExpect(view().name("checkout"));
	}

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testCheckoutForm_ClearCart_Success() throws Exception {
		mockMvc.perform(get("/cart/clear")).andExpect(status().isOk()).andExpect(view().name("checkout"));
	}

	// ========== Registration Form Tests ==========

	@Test
	public void testRegistrationForm_ValidData_Success() throws Exception {
		MockMultipartFile photoFile = new MockMultipartFile("photoFile", "avatar.jpg", MediaType.IMAGE_JPEG_VALUE,
				"avatar content".getBytes());

		mockMvc.perform(multipart("/register").file(photoFile).param("username", "newuser123")
				.param("password", "password123").param("fullname", "New User").param("email", "newuser@example.com")
				.param("address", "123 Test Street")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login?registerSuccess"));
	}

	@Test
	public void testRegistrationForm_WithoutPhoto_Success() throws Exception {
		MockMultipartFile emptyPhoto = new MockMultipartFile("photoFile", "", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

		mockMvc.perform(multipart("/register").file(emptyPhoto).param("username", "usernoavatar")
				.param("password", "password123").param("fullname", "User No Avatar")
				.param("email", "noavatar@example.com").param("address", "456 Test Avenue"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login?registerSuccess"));
	}

	@Test
	public void testRegistrationForm_DuplicateUsername_ValidationError() throws Exception {
		// First, create an account
		Account existingAccount = new Account();
		existingAccount.setUsername("existinguser");
		existingAccount.setPassword("password");
		existingAccount.setFullname("Existing User");
		existingAccount.setEmail("existing@example.com");
		existingAccount.setAddress("789 Test Road");
		existingAccount.setPhoto("default.jpg");
		accountServices.save(existingAccount);

		MockMultipartFile emptyPhoto = new MockMultipartFile("photoFile", "", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

		// Try to register with same username
		mockMvc.perform(multipart("/register").file(emptyPhoto).param("username", "existinguser")
				.param("password", "newpassword").param("fullname", "Another User")
				.param("email", "another@example.com").param("address", "999 Test Lane")).andExpect(status().isOk())
				.andExpect(view().name("register")).andExpect(model().attributeHasFieldErrors("account", "username"));
	}

	@Test
	public void testRegistrationForm_MissingRequiredFields_ValidationError() throws Exception {
		MockMultipartFile emptyPhoto = new MockMultipartFile("photoFile", "", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

		mockMvc.perform(multipart("/register").file(emptyPhoto).param("username", "").param("password", "")
				.param("fullname", "").param("email", "")).andExpect(status().isOk()).andExpect(view().name("register"))
				.andExpect(model().attributeHasErrors("account"));
	}

	// ========== Login Form Tests ==========

	@Test
	public void testLoginForm_ValidCredentials_Success() throws Exception {
		// Create a test user with plain text password (NoOpPasswordEncoder is used)
		Account testUser = new Account();
		testUser.setUsername("testuser");
		testUser.setPassword("testpass");
		testUser.setFullname("Test User");
		testUser.setEmail("test@example.com");
		testUser.setAddress("Test Address");
		testUser.setPhoto("default.jpg");
		testUser.setAdmin(false);
		accountServices.save(testUser);

		mockMvc.perform(post("/login").param("username", "testuser").param("password", "testpass"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/home"));
	}

	@Test
	public void testLoginForm_InvalidCredentials_Failure() throws Exception {
		mockMvc.perform(post("/login").param("username", "nonexistent").param("password", "wrongpassword"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login?error"));
	}

	@Test
	public void testLoginForm_MissingCredentials_Failure() throws Exception {
		mockMvc.perform(post("/login").param("username", "").param("password", ""))
				.andExpect(status().is3xxRedirection());
	}

	// ========== Form Validation Tests ==========

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testProductForm_NegativePrice_ValidationHandled() throws Exception {
		MockMultipartFile emptyFile = new MockMultipartFile("imageFile", "", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

		// The application should handle this gracefully
		mockMvc.perform(multipart("/dashboard/product/save").file(emptyFile).param("name", "Invalid Product")
				.param("price", "-50.0").param("amount", "10").param("category.id", testCategory.getId()))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testCartForm_InvalidQuantity_HandledGracefully() throws Exception {
		// Test with zero quantity
		mockMvc.perform(post("/cart/add").param("productId", testProduct.getId().toString()).param("quantity", "0"))
				.andExpect(status().is3xxRedirection());
	}

	// ========== CSRF Protection Tests ==========
	// Note: CSRF is disabled in SecurityConfig, so these tests verify the
	// application
	// works without CSRF protection (which is the current configuration)

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testProductForm_WithoutCSRF_WorksAsExpected() throws Exception {
		MockMultipartFile emptyFile = new MockMultipartFile("imageFile", "", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

		// Since CSRF is disabled, this should work fine
		mockMvc.perform(multipart("/dashboard/product/save").file(emptyFile).param("name", "Product")
				.param("price", "100.0").param("amount", "10").param("category.id", testCategory.getId()))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	public void testRegistrationForm_WithoutCSRF_WorksAsExpected() throws Exception {
		MockMultipartFile emptyPhoto = new MockMultipartFile("photoFile", "", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

		// Since CSRF is disabled, this should work fine
		mockMvc.perform(multipart("/register").file(emptyPhoto).param("username", "testuser2")
				.param("password", "password").param("fullname", "Test User").param("email", "test2@example.com")
				.param("address", "Test Address")).andExpect(status().is3xxRedirection());
	}

	// ========== Bootstrap Form Styling Tests ==========

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testProductFormPage_ContainsBootstrapClasses() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(org.hamcrest.Matchers.containsString("form-control")))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("form-label")));
	}

	@Test
	public void testRegistrationFormPage_ContainsBootstrapClasses() throws Exception {
		mockMvc.perform(get("/register")).andExpect(status().isOk())
				.andExpect(content().string(org.hamcrest.Matchers.containsString("form-control")))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("form-label")));
	}

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testCheckoutFormPage_ContainsBootstrapStyling() throws Exception {
		mockMvc.perform(get("/cart/checkout")).andExpect(status().isOk())
				.andExpect(content().string(org.hamcrest.Matchers.containsString("btn")))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("card")));
	}
}
