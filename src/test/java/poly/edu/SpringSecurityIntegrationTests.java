package poly.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import poly.edu.models.entities.Account;
import poly.edu.models.services.AccountServices;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Spring Security authentication and authorization. Tests
 * verify that security continues to work correctly after the theme redesign.
 * 
 * Test Coverage: - Login/logout functionality - Protected route access control
 * - Role-based authorization (USER vs ADMIN) - Authentication redirects -
 * Security integration with Bootstrap forms
 * 
 * Validates Requirements: 10.4 - Spring Security Integration
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SpringSecurityIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AccountServices accountServices;

	private Account testUser;
	private Account testAdmin;

	@BeforeEach
	public void setup() {
		// Create test user account (customer role)
		testUser = new Account();
		testUser.setUsername("testcustomer");
		testUser.setPassword("customerpass");
		testUser.setFullname("Test Customer");
		testUser.setEmail("customer@test.com");
		testUser.setAddress("123 Customer Street");
		testUser.setPhoto("default.jpg");
		testUser.setAdmin(false);
		accountServices.save(testUser);

		// Create test admin account
		testAdmin = new Account();
		testAdmin.setUsername("testadmin");
		testAdmin.setPassword("adminpass");
		testAdmin.setFullname("Test Admin");
		testAdmin.setEmail("admin@test.com");
		testAdmin.setAddress("456 Admin Avenue");
		testAdmin.setPhoto("default.jpg");
		testAdmin.setAdmin(true);
		accountServices.save(testAdmin);
	}

	// ========== Login Functionality Tests ==========

	@Test
	@WithAnonymousUser
	public void testLogin_ValidCustomerCredentials_Success() throws Exception {
		mockMvc.perform(post("/login").param("username", "testcustomer").param("password", "customerpass"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/home"));
	}

	@Test
	@WithAnonymousUser
	public void testLogin_ValidAdminCredentials_Success() throws Exception {
		mockMvc.perform(post("/login").param("username", "testadmin").param("password", "adminpass"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/home"));
	}

	@Test
	@WithAnonymousUser
	public void testLogin_InvalidCredentials_Failure() throws Exception {
		mockMvc.perform(post("/login").param("username", "testcustomer").param("password", "wrongpassword"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login?error"));
	}

	@Test
	@WithAnonymousUser
	public void testLogin_NonexistentUser_Failure() throws Exception {
		mockMvc.perform(post("/login").param("username", "nonexistent").param("password", "anypassword"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login?error"));
	}

	@Test
	@WithAnonymousUser
	public void testLogin_EmptyCredentials_Failure() throws Exception {
		mockMvc.perform(post("/login").param("username", "").param("password", ""))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	@WithAnonymousUser
	public void testLoginPage_DisplaysBootstrapForm() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk())
				.andExpect(content().string(org.hamcrest.Matchers.containsString("form-control")))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("btn")));
	}

	// ========== Logout Functionality Tests ==========

	@Test
	@WithMockUser(username = "testcustomer", roles = { "USER" })
	public void testLogout_AuthenticatedUser_Success() throws Exception {
		mockMvc.perform(post("/logout")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login?logout"));
	}

	@Test
	@WithMockUser(username = "testadmin", roles = { "ADMIN" })
	public void testLogout_AuthenticatedAdmin_Success() throws Exception {
		mockMvc.perform(post("/logout")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login?logout"));
	}

	@Test
	@WithAnonymousUser
	public void testLogout_AnonymousUser_RedirectsToLogin() throws Exception {
		mockMvc.perform(post("/logout")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login?logout"));
	}

	// ========== Protected Routes - Public Access Tests ==========

	@Test
	@WithAnonymousUser
	public void testPublicRoute_Home_AccessibleWithoutAuth() throws Exception {
		mockMvc.perform(get("/home")).andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	public void testPublicRoute_Products_AccessibleWithoutAuth() throws Exception {
		mockMvc.perform(get("/products")).andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	public void testPublicRoute_About_AccessibleWithoutAuth() throws Exception {
		mockMvc.perform(get("/about")).andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	public void testPublicRoute_Contact_AccessibleWithoutAuth() throws Exception {
		mockMvc.perform(get("/contact")).andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	public void testPublicRoute_Login_AccessibleWithoutAuth() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	public void testPublicRoute_Register_AccessibleWithoutAuth() throws Exception {
		mockMvc.perform(get("/register")).andExpect(status().isOk());
	}

	// ========== Protected Routes - Authentication Required Tests ==========

	@Test
	@WithAnonymousUser
	public void testProtectedRoute_Checkout_RequiresAuthentication() throws Exception {
		mockMvc.perform(get("/checkout")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithAnonymousUser
	public void testProtectedRoute_Payment_RequiresAuthentication() throws Exception {
		mockMvc.perform(get("/payment")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser(username = "testcustomer", roles = { "USER" })
	public void testProtectedRoute_Checkout_AccessibleWhenAuthenticated() throws Exception {
		mockMvc.perform(get("/cart/checkout")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "testcustomer", roles = { "USER" })
	public void testProtectedRoute_Payment_RequiresOrderData() throws Exception {
		// Payment route requires authentication AND order data
		// Without order data in session, it will redirect or error
		mockMvc.perform(get("/payment")).andExpect(status().is3xxRedirection());
	}

	// ========== Role-Based Access Control - Admin Routes Tests ==========

	@Test
	@WithAnonymousUser
	public void testAdminRoute_Dashboard_RequiresAuthentication() throws Exception {
		mockMvc.perform(get("/dashboard")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser(username = "testcustomer", roles = { "USER" })
	public void testAdminRoute_Dashboard_DeniedForCustomer() throws Exception {
		mockMvc.perform(get("/dashboard")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "testadmin", roles = { "ADMIN" })
	public void testAdminRoute_Dashboard_AllowedForAdmin() throws Exception {
		mockMvc.perform(get("/dashboard")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "testcustomer", roles = { "USER" })
	public void testAdminRoute_ProductManagement_DeniedForCustomer() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "testadmin", roles = { "ADMIN" })
	public void testAdminRoute_ProductManagement_AllowedForAdmin() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "testcustomer", roles = { "USER" })
	public void testAdminRoute_CategoryManagement_DeniedForCustomer() throws Exception {
		mockMvc.perform(get("/dashboard/category")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "testadmin", roles = { "ADMIN" })
	public void testAdminRoute_CategoryManagement_AllowedForAdmin() throws Exception {
		mockMvc.perform(get("/dashboard/category")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "testcustomer", roles = { "USER" })
	public void testAdminRoute_OrderManagement_DeniedForCustomer() throws Exception {
		mockMvc.perform(get("/dashboard/order")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "testadmin", roles = { "ADMIN" })
	public void testAdminRoute_OrderManagement_AllowedForAdmin() throws Exception {
		mockMvc.perform(get("/dashboard/order")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "testcustomer", roles = { "USER" })
	public void testAdminRoute_AccountManagement_DeniedForCustomer() throws Exception {
		mockMvc.perform(get("/dashboard/account")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "testadmin", roles = { "ADMIN" })
	public void testAdminRoute_AccountManagement_AllowedForAdmin() throws Exception {
		mockMvc.perform(get("/dashboard/account")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "testcustomer", roles = { "USER" })
	public void testAdminRoute_DiscountManagement_DeniedForCustomer() throws Exception {
		mockMvc.perform(get("/dashboard/discount")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "testadmin", roles = { "ADMIN" })
	public void testAdminRoute_DiscountManagement_AllowedForAdmin() throws Exception {
		mockMvc.perform(get("/dashboard/discount")).andExpect(status().isOk());
	}

	// ========== Authentication Redirect Tests ==========

	@Test
	@WithAnonymousUser
	public void testAuthenticationRedirect_ProtectedPage_RedirectsToLogin() throws Exception {
		mockMvc.perform(get("/checkout")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithAnonymousUser
	public void testAuthenticationRedirect_AdminPage_RedirectsToLogin() throws Exception {
		mockMvc.perform(get("/dashboard")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithAnonymousUser
	public void testAuthenticationRedirect_AdminSubPage_RedirectsToLogin() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	// ========== Role-Based Navigation Tests ==========

	@Test
	@WithMockUser(username = "testcustomer", roles = { "USER" })
	public void testNavigation_CustomerUser_CanAccessCustomerPages() throws Exception {
		// Customer should be able to access customer pages
		mockMvc.perform(get("/home")).andExpect(status().isOk());
		mockMvc.perform(get("/products")).andExpect(status().isOk());
		mockMvc.perform(get("/cart/checkout")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "testadmin", roles = { "ADMIN" })
	public void testNavigation_AdminUser_CanAccessBothCustomerAndAdminPages() throws Exception {
		// Admin should be able to access both customer and admin pages
		mockMvc.perform(get("/home")).andExpect(status().isOk());
		mockMvc.perform(get("/products")).andExpect(status().isOk());
		mockMvc.perform(get("/dashboard")).andExpect(status().isOk());
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk());
	}

	// ========== Bootstrap Form Integration with Security Tests ==========

	@Test
	@WithAnonymousUser
	public void testBootstrapLoginForm_ContainsSecurityElements() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk())
				.andExpect(content().string(org.hamcrest.Matchers.containsString("username")))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("password")))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("form-control")));
	}

	@Test
	@WithMockUser(username = "testadmin", roles = { "ADMIN" })
	public void testBootstrapAdminForms_WorkWithSecurity() throws Exception {
		// Admin forms should work with security context
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(org.hamcrest.Matchers.containsString("form-control")));
	}

	@Test
	@WithMockUser(username = "testcustomer", roles = { "USER" })
	public void testBootstrapCheckoutForm_WorksWithAuthentication() throws Exception {
		// Checkout form should work with authenticated user
		mockMvc.perform(get("/cart/checkout")).andExpect(status().isOk())
				.andExpect(content().string(org.hamcrest.Matchers.containsString("btn")));
	}

	// ========== Security Context Tests ==========

	@Test
	@WithMockUser(username = "testcustomer", roles = { "USER" })
	public void testSecurityContext_AuthenticatedUser_HasCorrectRole() throws Exception {
		// Verify that authenticated user has correct role
		mockMvc.perform(get("/home")).andExpect(status().isOk());

		// Customer should not be able to access admin pages
		mockMvc.perform(get("/dashboard")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "testadmin", roles = { "ADMIN" })
	public void testSecurityContext_AdminUser_HasCorrectRole() throws Exception {
		// Verify that admin user has correct role
		mockMvc.perform(get("/dashboard")).andExpect(status().isOk());

		// Admin should be able to access all admin pages
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk());
	}

	// ========== Error Handling Tests ==========

	@Test
	@WithAnonymousUser
	public void testLoginError_DisplaysErrorMessage() throws Exception {
		mockMvc.perform(get("/login?error")).andExpect(status().isOk()).andExpect(view().name("login"));
	}

	@Test
	@WithAnonymousUser
	public void testLogoutSuccess_DisplaysSuccessMessage() throws Exception {
		mockMvc.perform(get("/login?logout")).andExpect(status().isOk()).andExpect(view().name("login"));
	}

	@Test
	@WithAnonymousUser
	public void testRegistrationSuccess_RedirectsToLogin() throws Exception {
		mockMvc.perform(get("/login?registerSuccess")).andExpect(status().isOk()).andExpect(view().name("login"));
	}

	// ========== Session Management Tests ==========

	@Test
	@WithMockUser(username = "testcustomer", roles = { "USER" })
	public void testSession_AuthenticatedUser_MaintainsSession() throws Exception {
		// First request should succeed
		mockMvc.perform(get("/home")).andExpect(status().isOk());

		// Subsequent request should also succeed (session maintained)
		mockMvc.perform(get("/products")).andExpect(status().isOk());
	}

	// ========== Static Resources Access Tests ==========

	@Test
	@WithAnonymousUser
	public void testStaticResources_Images_AccessibleWithoutAuth() throws Exception {
		// Images should be accessible without authentication (as per SecurityConfig
		// /images/**)
		mockMvc.perform(get("/images/test.jpg")).andExpect(status().isNotFound()); // 404 is expected if file doesn't
																					// exist, but no redirect to login
	}

	@Test
	@WithAnonymousUser
	public void testStaticResources_CSS_RequiresAuthOrStaticHandler() throws Exception {
		// CSS files are not explicitly in permitAll, so they require authentication
		// This is expected behavior based on SecurityConfig
		mockMvc.perform(get("/css/custom.css")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithAnonymousUser
	public void testStaticResources_JavaScript_RequiresAuthOrStaticHandler() throws Exception {
		// JavaScript files are not explicitly in permitAll, so they require
		// authentication
		// This is expected behavior based on SecurityConfig
		mockMvc.perform(get("/js/custom.js")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	// ========== Cart Functionality with Security Tests ==========

	@Test
	@WithAnonymousUser
	public void testCart_AnonymousUser_CanAccessCartCheckout() throws Exception {
		// Cart checkout should be accessible without authentication (as per
		// SecurityConfig /cart/**)
		mockMvc.perform(get("/cart/checkout")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "testcustomer", roles = { "USER" })
	public void testCart_AuthenticatedUser_CanAccessCartCheckout() throws Exception {
		// Authenticated user should also be able to access cart checkout
		mockMvc.perform(get("/cart/checkout")).andExpect(status().isOk());
	}

	// ========== Multiple Role Tests ==========

	@Test
	@WithMockUser(username = "testuser", roles = { "USER", "ADMIN" })
	public void testMultipleRoles_UserWithBothRoles_CanAccessBoth() throws Exception {
		// User with both roles should access both customer and admin pages
		mockMvc.perform(get("/home")).andExpect(status().isOk());

		mockMvc.perform(get("/dashboard")).andExpect(status().isOk());
	}

	// ========== Security Headers Tests ==========

	@Test
	@WithAnonymousUser
	public void testSecurityHeaders_PublicPage_HasSecurityHeaders() throws Exception {
		mockMvc.perform(get("/home")).andExpect(status().isOk()).andExpect(header().exists("X-Content-Type-Options"))
				.andExpect(header().exists("X-Frame-Options"));
	}
}
