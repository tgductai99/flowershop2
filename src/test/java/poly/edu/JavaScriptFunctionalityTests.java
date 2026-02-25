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
import poly.edu.models.entities.Discount;
import poly.edu.models.entities.Product;
import poly.edu.models.services.AccountServices;
import poly.edu.models.services.CategoryServices;
import poly.edu.models.services.DiscountServices;
import poly.edu.models.services.ProductServices;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for JavaScript functionality in the AccessMotor
 * application. Tests verify that all JavaScript features work correctly after
 * the theme redesign.
 * 
 * This includes: - Cart add/remove functionality - Modal dialogs (Bootstrap
 * modals) - Form validation (Bootstrap validation) - Sidebar toggle (admin
 * dashboard) - Navbar collapse on mobile (Bootstrap navbar)
 * 
 * Validates Requirements: 10.3 - Functionality Preservation (JavaScript)
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class JavaScriptFunctionalityTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductServices productServices;

	@Autowired
	private CategoryServices categoryServices;

	@Autowired
	private AccountServices accountServices;

	@Autowired
	private DiscountServices discountServices;

	private Category testCategory;
	private Product testProduct;
	private Account testAccount;

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
		testProduct.setAvailable(true);
		testProduct.setCategory(testCategory);
		productServices.save(testProduct);

		// Create test account
		testAccount = new Account();
		testAccount.setUsername("testuser");
		testAccount.setPassword("password");
		testAccount.setFullname("Test User");
		testAccount.setEmail("test@example.com");
		testAccount.setAddress("Test Address");
		testAccount.setPhoto("default.jpg");
		testAccount.setAdmin(false);
		accountServices.save(testAccount);
	}

	// ========== Cart Add/Remove Functionality Tests ==========

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testCartAddFunctionality_AddToCart_Success() throws Exception {
		// Test that cart add endpoint works (JavaScript would call this)
		mockMvc.perform(post("/cart/add").param("productId", testProduct.getId().toString()).param("quantity", "1"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/products"));
	}

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testCartAddFunctionality_AddMultipleQuantity_Success() throws Exception {
		mockMvc.perform(post("/cart/add").param("productId", testProduct.getId().toString()).param("quantity", "3"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/products"));
	}

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testCartRemoveFunctionality_RemoveItem_Success() throws Exception {
		// First add item to cart
		mockMvc.perform(post("/cart/add").param("productId", testProduct.getId().toString()).param("quantity", "1"));

		// Then remove it
		mockMvc.perform(get("/cart/remove").param("productId", testProduct.getId().toString()))
				.andExpect(status().isOk()).andExpect(view().name("checkout"));
	}

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testCartClearFunctionality_ClearCart_Success() throws Exception {
		// Add items to cart
		mockMvc.perform(post("/cart/add").param("productId", testProduct.getId().toString()).param("quantity", "2"));

		// Clear cart
		mockMvc.perform(get("/cart/clear")).andExpect(status().isOk()).andExpect(view().name("checkout"));
	}

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testCartBuyNowFunctionality_BuyNow_RedirectsToCheckout() throws Exception {
		mockMvc.perform(post("/cart/buy").param("productId", testProduct.getId().toString()).param("quantity", "1"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/cart/checkout"));
	}

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testCartPage_ContainsCartButtons() throws Exception {
		mockMvc.perform(get("/cart/checkout")).andExpect(status().isOk())
				.andExpect(content().string(containsString("btn"))).andExpect(content().string(containsString("cart")));
	}

	// ========== Modal Dialog Tests ==========

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testModalDialog_ProductDashboard_ContainsModalMarkup() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("modal")))
				.andExpect(content().string(containsString("data-bs-toggle=\"modal\"")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testModalDialog_ProductDashboard_ContainsDeleteModal() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("deleteModal")))
				.andExpect(content().string(containsString("modal-dialog")))
				.andExpect(content().string(containsString("modal-content")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testModalDialog_ProductDashboard_ContainsEditModal() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("productFormModal")))
				.andExpect(content().string(containsString("modal-header")))
				.andExpect(content().string(containsString("modal-body")))
				.andExpect(content().string(containsString("modal-footer")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testModalDialog_CategoryDashboard_ContainsModalMarkup() throws Exception {
		mockMvc.perform(get("/dashboard/category")).andExpect(status().isOk())
				.andExpect(content().string(containsString("modal")))
				.andExpect(content().string(containsString("categoryFormModal")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testModalDialog_AccountDashboard_ContainsModalMarkup() throws Exception {
		mockMvc.perform(get("/dashboard/account")).andExpect(status().isOk())
				.andExpect(content().string(containsString("modal")))
				.andExpect(content().string(containsString("accountFormModal")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testModalDialog_DiscountDashboard_ContainsModalMarkup() throws Exception {
		mockMvc.perform(get("/dashboard/discount")).andExpect(status().isOk())
				.andExpect(content().string(containsString("modal")))
				.andExpect(content().string(containsString("discountFormModal")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testModalDialog_ContainsBootstrapModalClasses() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("modal fade")))
				.andExpect(content().string(containsString("modal-dialog")))
				.andExpect(content().string(containsString("modal-content")))
				.andExpect(content().string(containsString("btn-close")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testModalDialog_ContainsDataBsAttributes() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("data-bs-toggle")))
				.andExpect(content().string(containsString("data-bs-target")))
				.andExpect(content().string(containsString("data-bs-dismiss")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testModalDialog_ProductEdit_TriggersModalWithFlashAttribute() throws Exception {
		// Edit action should redirect with flash attribute that triggers modal
		mockMvc.perform(get("/dashboard/product/edit/" + testProduct.getId())).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/dashboard/product")).andExpect(flash().attributeExists("product"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testModalDialog_CategoryEdit_TriggersModalWithFlashAttribute() throws Exception {
		mockMvc.perform(get("/dashboard/category/edit/" + testCategory.getId())).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/dashboard/category")).andExpect(flash().attributeExists("category"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testModalDialog_AccountEdit_TriggersModalWithFlashAttribute() throws Exception {
		mockMvc.perform(get("/dashboard/account/edit/" + testAccount.getUsername()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/dashboard/account"))
				.andExpect(flash().attributeExists("account"));
	}

	// ========== Form Validation Tests ==========

	@Test
	public void testFormValidation_ContactForm_ContainsValidationMarkup() throws Exception {
		mockMvc.perform(get("/contact")).andExpect(status().isOk())
				.andExpect(content().string(containsString("contactForm")))
				.andExpect(content().string(containsString("required")));
	}

	@Test
	public void testFormValidation_RegisterForm_ContainsValidationMarkup() throws Exception {
		mockMvc.perform(get("/register")).andExpect(status().isOk())
				.andExpect(content().string(containsString("form-control")))
				.andExpect(content().string(containsString("onchange=\"previewImage")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testFormValidation_ProductForm_ContainsValidationClasses() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("form-control")))
				.andExpect(content().string(containsString("form-label")))
				.andExpect(content().string(containsString("required")));
	}

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testFormValidation_CheckoutForm_ContainsValidationMarkup() throws Exception {
		mockMvc.perform(get("/cart/checkout")).andExpect(status().isOk())
				.andExpect(content().string(containsString("card")));
	}

	@Test
	public void testFormValidation_ContactForm_ContainsValidationScript() throws Exception {
		mockMvc.perform(get("/contact")).andExpect(status().isOk())
				.andExpect(content().string(containsString("script")))
				.andExpect(content().string(containsString("contactForm")));
	}

	@Test
	public void testFormValidation_RegisterForm_ContainsImagePreviewScript() throws Exception {
		mockMvc.perform(get("/register")).andExpect(status().isOk())
				.andExpect(content().string(containsString("<script")))
				.andExpect(content().string(containsString("previewImage")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testFormValidation_ProductForm_ContainsImagePreviewScript() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("previewImage")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testFormValidation_DiscountForm_ContainsDiscountTypeScript() throws Exception {
		mockMvc.perform(get("/dashboard/discount")).andExpect(status().isOk())
				.andExpect(content().string(containsString("discountType")))
				.andExpect(content().string(containsString("discountValue")));
	}

	// ========== Sidebar Toggle Tests ==========

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testSidebarToggle_AdminLayout_ContainsSidebarMarkup() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("sidebar")))
				.andExpect(content().string(containsString("sidebarToggle")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testSidebarToggle_AdminLayout_ContainsToggleButton() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("id=\"sidebarToggle\"")))
				.andExpect(content().string(containsString("btn")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testSidebarToggle_AdminLayout_ContainsSidebarNavigation() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("class=\"sidebar")))
				.andExpect(content().string(containsString("nav-link")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testSidebarToggle_CustomJS_LoadedOnAdminPages() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("/js/custom.js")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testSidebarToggle_AllAdminPages_ContainSidebar() throws Exception {
		// Test all admin pages have sidebar
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("sidebar")));

		mockMvc.perform(get("/dashboard/category")).andExpect(status().isOk())
				.andExpect(content().string(containsString("sidebar")));

		mockMvc.perform(get("/dashboard/order")).andExpect(status().isOk())
				.andExpect(content().string(containsString("sidebar")));

		mockMvc.perform(get("/dashboard/account")).andExpect(status().isOk())
				.andExpect(content().string(containsString("sidebar")));

		mockMvc.perform(get("/dashboard/discount")).andExpect(status().isOk())
				.andExpect(content().string(containsString("sidebar")));
	}

	// ========== Navbar Collapse Tests ==========

	@Test
	public void testNavbarCollapse_CustomerHeader_ContainsCollapseMarkup() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk())
				.andExpect(content().string(containsString("navbar-toggler")))
				.andExpect(content().string(containsString("navbar-collapse")))
				.andExpect(content().string(containsString("collapse")));
	}

	@Test
	public void testNavbarCollapse_CustomerHeader_ContainsToggleButton() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk())
				.andExpect(content().string(containsString("navbar-toggler")))
				.andExpect(content().string(containsString("data-bs-toggle=\"collapse\"")))
				.andExpect(content().string(containsString("navbar-toggler-icon")));
	}

	@Test
	public void testNavbarCollapse_CustomerHeader_ContainsCollapseTarget() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk())
				.andExpect(content().string(containsString("data-bs-target")))
				.andExpect(content().string(containsString("navbarNav")));
	}

	@Test
	public void testNavbarCollapse_CustomerHeader_ContainsBootstrapClasses() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string(containsString("navbar")))
				.andExpect(content().string(containsString("navbar-expand-lg")))
				.andExpect(content().string(containsString("navbar-collapse")));
	}

	@Test
	public void testNavbarCollapse_AllCustomerPages_ContainNavbar() throws Exception {
		// Test all customer pages have collapsible navbar
		mockMvc.perform(get("/")).andExpect(status().isOk())
				.andExpect(content().string(containsString("navbar-toggler")));

		mockMvc.perform(get("/products")).andExpect(status().isOk())
				.andExpect(content().string(containsString("navbar-toggler")));

		mockMvc.perform(get("/about")).andExpect(status().isOk())
				.andExpect(content().string(containsString("navbar-toggler")));

		mockMvc.perform(get("/contact")).andExpect(status().isOk())
				.andExpect(content().string(containsString("navbar-toggler")));
	}

	// ========== Bootstrap JavaScript Integration Tests ==========

	@Test
	public void testBootstrapJS_CustomerPages_LoadsBootstrapBundle() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk())
				.andExpect(content().string(containsString("bootstrap.bundle.min.js")))
				.andExpect(content().string(containsString("cdn.jsdelivr.net")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testBootstrapJS_AdminPages_LoadsBootstrapBundle() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("bootstrap.bundle.min.js")))
				.andExpect(content().string(containsString("cdn.jsdelivr.net")));
	}

	@Test
	public void testBootstrapJS_CustomerPages_LoadsCustomJS() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk())
				.andExpect(content().string(containsString("/js/custom.js")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testBootstrapJS_AdminPages_LoadsCustomJS() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("/js/custom.js")));
	}

	@Test
	public void testBootstrapJS_CustomerPages_HasFallbackMechanism() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string(containsString("onerror")))
				.andExpect(content().string(containsString("/js/bootstrap.bundle.min.js")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testBootstrapJS_AdminPages_HasFallbackMechanism() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("onerror")))
				.andExpect(content().string(containsString("/js/bootstrap.bundle.min.js")));
	}

	// ========== Auto-Hide Alerts Tests ==========

	@Test
	public void testAutoHideAlerts_CheckoutPage_ContainsAutoHideScript() throws Exception {
		mockMvc.perform(get("/cart/checkout")).andExpect(status().isOk())
				.andExpect(content().string(containsString("auto-hide")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testAutoHideAlerts_AdminPages_ContainsAutoHideScript() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("auto-hide")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testAutoHideAlerts_AdminPages_ContainsAlertContainer() throws Exception {
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("alert")));
	}

	// ========== JavaScript Functionality Integration Tests ==========

	@Test
	@WithMockUser(username = "customer", roles = { "USER" })
	public void testJavaScriptIntegration_CartWorkflow_AllComponentsPresent() throws Exception {
		// Test that all JavaScript components needed for cart workflow are present
		mockMvc.perform(get("/products")).andExpect(status().isOk())
				.andExpect(content().string(containsString("bootstrap.bundle.min.js")))
				.andExpect(content().string(containsString("/js/custom.js")))
				.andExpect(content().string(containsString("btn")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testJavaScriptIntegration_AdminWorkflow_AllComponentsPresent() throws Exception {
		// Test that all JavaScript components needed for admin workflow are present
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("bootstrap.bundle.min.js")))
				.andExpect(content().string(containsString("/js/custom.js")))
				.andExpect(content().string(containsString("modal")))
				.andExpect(content().string(containsString("sidebar")))
				.andExpect(content().string(containsString("sidebarToggle")));
	}

	@Test
	public void testJavaScriptIntegration_FormValidation_AllComponentsPresent() throws Exception {
		// Test that all JavaScript components needed for form validation are present
		mockMvc.perform(get("/contact")).andExpect(status().isOk())
				.andExpect(content().string(containsString("bootstrap.bundle.min.js")))
				.andExpect(content().string(containsString("contactForm")))
				.andExpect(content().string(containsString("script")));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testJavaScriptIntegration_ModalWorkflow_AllComponentsPresent() throws Exception {
		// Test that all JavaScript components needed for modal workflow are present
		mockMvc.perform(get("/dashboard/product")).andExpect(status().isOk())
				.andExpect(content().string(containsString("bootstrap.bundle.min.js")))
				.andExpect(content().string(containsString("modal")))
				.andExpect(content().string(containsString("data-bs-toggle=\"modal\"")))
				.andExpect(content().string(containsString("data-bs-target")));
	}
}
