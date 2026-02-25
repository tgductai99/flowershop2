package poly.edu.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import poly.edu.models.entities.Account;
import poly.edu.models.entities.Category;
import poly.edu.models.entities.Order;
import poly.edu.models.entities.OrderDetail;
import poly.edu.models.entities.Product;
import poly.edu.models.services.AccountServices;
import poly.edu.models.services.CategoryServices;
import poly.edu.models.services.OrderServices;
import poly.edu.models.services.ProductServices;
import poly.edu.utils.ImageUtil;
import poly.edu.utils.PaginationUtil;

@Controller
@SessionAttributes("order")
public class ClientController {

	// PathVariable: /cart/remove/{id}
	// RequestParam: /cart/remove(id=${item.id})

	@Autowired
	private ProductServices productServices;

	@Autowired
	private CategoryServices categoryServices;

	@Autowired
	private OrderServices orderServices;

	@Autowired
	private AccountServices accountServices;

	@ModelAttribute("products")
	public List<Product> getProducts() {
		return productServices.findAll();
	}

	@ModelAttribute("categories")
	public List<Category> getCategories() {
		return categoryServices.findAll();
	}

	@ModelAttribute("order")
	public Order initCart() {
		return new Order();
	}

	@GetMapping(value = { "/", "/home" })
	public String home(Model model, @RequestParam(required = false) String keyword,
			@RequestParam(required = false) String categoryId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "4") int size) {
		List<Product> filtered = productServices.filter(keyword, categoryId);
		List<Product> products = PaginationUtil.paginate(filtered, page, size);
		int pages = PaginationUtil.getTotalPages(filtered, size);

		model.addAttribute("pages", pages);
		model.addAttribute("page", page);
		model.addAttribute("products", products);

		return "home";
	}

	@RequestMapping("/contact")
	public String contact(Model model, Authentication authentication) {
		// Pre-fill contact form with user information if logged in
		if (authentication != null && authentication.isAuthenticated()) {
			UserDetails user = (UserDetails) authentication.getPrincipal();
			Account account = accountServices.findById(user.getUsername());
			if (account != null) {
				model.addAttribute("account", account);
			}
		}
		return "contact";
	}

	@RequestMapping("/about")
	public String about() {

		return "about";
	}

	@GetMapping("/products")
	public String products(Model model, @RequestParam(required = false) String keyword,
			@RequestParam(required = false) String categoryId, @RequestParam(required = false) String sortBy,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size) {

		List<Product> filtered = productServices.filterAndSort(keyword, categoryId, sortBy);
		List<Product> products = PaginationUtil.paginate(filtered, page, size);
		int pages = PaginationUtil.getTotalPages(filtered, size);

		model.addAttribute("categoryId", categoryId);
		model.addAttribute("keyword", keyword);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("pages", pages);
		model.addAttribute("page", page);
		model.addAttribute("products", products);

		return "products";
	}

	@GetMapping("/products/{id}")
	public String productDetail(@PathVariable Integer id, Model model) {
		Product product = productServices.findById(id);
		if (product == null) {
			return "redirect:/products";
		}
		model.addAttribute("product", product);
		return "product-detail";
	}

	@GetMapping("/cart/checkout")
	public String checkout(Model model, Authentication authentication) {
		// Pre-fill address with user's account address if available
		if (authentication != null && authentication.isAuthenticated()) {
			UserDetails user = (UserDetails) authentication.getPrincipal();
			Account account = accountServices.findById(user.getUsername());
			if (account != null && account.getAddress() != null) {
				model.addAttribute("defaultAddress", account.getAddress());
			}
		}
		return "checkout";
	}

	@PostMapping("/cart/add")
	public String addToCart(@RequestParam Integer productId, 
			@RequestParam Integer quantity,
			@ModelAttribute("order") Order order, 
			RedirectAttributes redirect) {

		try {
			order = orderServices.addOrderDetailToOrder(order, productId, quantity);
			redirect.addFlashAttribute("message", "addSuccess");
			redirect.addFlashAttribute("addedProductId", productId);
		} catch (RuntimeException e) {
			String errorMessage = e.getMessage();
			if (errorMessage.contains("not available")) {
				redirect.addFlashAttribute("message", "notAvailable");
			} else if (errorMessage.contains("Insufficient stock")) {
				redirect.addFlashAttribute("message", "insufficientStock");
			} else if (errorMessage.contains("not found")) {
				redirect.addFlashAttribute("message", "productNotFound");
			} else if (errorMessage.contains("Invalid quantity")) {
				redirect.addFlashAttribute("message", "invalidQuantity");
			} else {
				redirect.addFlashAttribute("message", "addFail");
			}
		}

		return "redirect:/cart/checkout";
	}

	@PostMapping("/cart/buy")
	public String buyNow(@RequestParam Integer productId, @RequestParam Integer quantity,
			@ModelAttribute("order") Order order, RedirectAttributes redirect) {

		try {
			order = orderServices.addOrderDetailToOrder(order, productId, quantity);
		} catch (RuntimeException e) {
			String errorMessage = e.getMessage();
			if (errorMessage.contains("not available")) {
				redirect.addFlashAttribute("message", "notAvailable");
			} else if (errorMessage.contains("Insufficient stock")) {
				redirect.addFlashAttribute("message", "insufficientStock");
			} else if (errorMessage.contains("not found")) {
				redirect.addFlashAttribute("message", "productNotFound");
			} else if (errorMessage.contains("Invalid quantity")) {
				redirect.addFlashAttribute("message", "invalidQuantity");
			} else {
				redirect.addFlashAttribute("message", "addFail");
			}
			return "redirect:/cart/checkout";
		}

		return "redirect:/cart/checkout";
	}

	@GetMapping("/cart/remove")
	public String removeItem(@RequestParam Integer productId, @ModelAttribute("order") Order order) {
		order.getOrderDetails().removeIf(od -> od.getProduct().getId().equals(productId));
		return "checkout";
	}

	@GetMapping("/cart/clear")
	public String clearItems(@ModelAttribute("order") Order order) {
		order.getOrderDetails().clear(); // object store in session can't be reassigned
		return "checkout";
	}

	@PostMapping("/cart/update")
	public String updateQuantity(@RequestParam Integer productId, @RequestParam Integer quantity,
			@ModelAttribute("order") Order order, RedirectAttributes redirect) {
		try {
			// If quantity is 0 or less, remove the item
			if (quantity <= 0) {
				order.getOrderDetails().removeIf(od -> od.getProduct().getId().equals(productId));
				redirect.addFlashAttribute("message", "quantityUpdated");
				return "redirect:/cart/checkout";
			}

			// Find the order detail for this product
			for (OrderDetail od : order.getOrderDetails()) {
				if (od.getProduct().getId().equals(productId)) {
					// Get fresh product data to check current stock
					Product product = productServices.findById(productId);

					// Check if requested quantity exceeds available stock
					if (quantity > product.getAmount()) {
						redirect.addFlashAttribute("message", "quantityExceeded");
						return "redirect:/cart/checkout";
					}

					// Update quantity
					od.setQuantity(quantity);
					redirect.addFlashAttribute("message", "quantityUpdated");
					return "redirect:/cart/checkout";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute("message", "saveFail");
		}

		return "redirect:/cart/checkout";
	}

	@PostMapping("/payment")
		public String payment(Model model, @ModelAttribute("order") Order order,
				@RequestParam String address, Authentication authentication) {

			try {
				// Validate address
				if (address == null || address.trim().isEmpty()) {
					model.addAttribute("message", "addressRequired");
					return "redirect:/cart/checkout";
				}

				// Validate cart is not empty
				if (order.getOrderDetails().isEmpty()) {
					model.addAttribute("message", "emptyCart");
					return "redirect:/cart/checkout";
				}

				// Validate stock availability
				if (!orderServices.validateStock(order)) {
					model.addAttribute("message", "insufficientStock");
					return "redirect:/cart/checkout";
				}

				UserDetails user = (UserDetails) authentication.getPrincipal();
				Account account = accountServices.findById(user.getUsername());

				// Check if phone and address are provided
				boolean missingPhone = account.getPhone() == null || account.getPhone().trim().isEmpty();
				boolean missingAddress = address == null || address.trim().isEmpty();

				// Calculate order total
				Double orderTotal = order.getOrderDetails().stream()
						.mapToDouble(od -> od.getPrice() * od.getQuantity())
						.sum();

				// Pass data to payment confirmation page
				model.addAttribute("account", account);
				model.addAttribute("order", order);
				model.addAttribute("deliveryAddress", address);
				model.addAttribute("orderTotal", orderTotal);
				model.addAttribute("missingPhone", missingPhone);
				model.addAttribute("missingAddress", missingAddress);
				model.addAttribute("currentPage", "checkout");

				return "payment-confirmation";

			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("message", "saveFail");
				return "redirect:/cart/checkout";
			}
		}

		@PostMapping("/payment/confirm")
		public String confirmPayment(RedirectAttributes redirect, @ModelAttribute("order") Order order,
				@RequestParam String address, Authentication authentication) {

			try {
				// Validate address
				if (address == null || address.trim().isEmpty()) {
					redirect.addFlashAttribute("message", "addressRequired");
					return "redirect:/cart/checkout";
				}

				// Validate cart is not empty
				if (order.getOrderDetails().isEmpty()) {
					redirect.addFlashAttribute("message", "emptyCart");
					return "redirect:/cart/checkout";
				}

				// Validate stock availability
				if (!orderServices.validateStock(order)) {
					redirect.addFlashAttribute("message", "insufficientStock");
					return "redirect:/cart/checkout";
				}

				UserDetails user = (UserDetails) authentication.getPrincipal();
				Account account = accountServices.findById(user.getUsername());

				// Final validation: phone and address must be present
				if (account.getPhone() == null || account.getPhone().trim().isEmpty()) {
					redirect.addFlashAttribute("message", "phoneRequired");
					return "redirect:/cart/checkout";
				}

				// Create a new Order instance to avoid detached entity issue
				Order newOrder = new Order();
				newOrder.setAccount(account);
				newOrder.setAddress(address.trim());
				newOrder.setCreateDate(java.time.LocalDate.now());

				// Copy order details to new order
				for (OrderDetail od : order.getOrderDetails()) {
					OrderDetail newOd = new OrderDetail();
					newOd.setProduct(od.getProduct());
					newOd.setPrice(od.getPrice());
					newOd.setQuantity(od.getQuantity());
					newOd.setOrder(newOrder);
					newOrder.getOrderDetails().add(newOd);
				}

				// Save order
				orderServices.save(newOrder);

				// Update product quantities
				for (OrderDetail od : newOrder.getOrderDetails()) {
					Product product = od.getProduct();
					int newAmount = product.getAmount() - od.getQuantity();
					product.setAmount(newAmount);
					productServices.save(product);
				}

				// Clear cart after successful order
				order.getOrderDetails().clear();

				redirect.addFlashAttribute("message", "saveTrue");
			} catch (Exception e) {
				e.printStackTrace();
				redirect.addFlashAttribute("message", "saveFail");
				return "redirect:/cart/checkout";
			}

			return "redirect:/profile";
		}


	@GetMapping("/profile")
	public String profile(Model model, Authentication authentication) {
		if (authentication != null && authentication.isAuthenticated()) {
			UserDetails user = (UserDetails) authentication.getPrincipal();
			Account account = accountServices.findById(user.getUsername());
			model.addAttribute("account", account);

			// Get user's orders - use the account's orders relationship
			if (account.getOrders() != null) {
				model.addAttribute("userOrders", account.getOrders());
			} else {
				model.addAttribute("userOrders", List.of());
			}
		}
		return "profile";
	}

	@PostMapping("/profile/update")
	public String updateProfile(@RequestParam String fullname, @RequestParam String email,
			@RequestParam(required = false) String phone, @RequestParam(required = false) String address,
			@RequestParam(required = false) MultipartFile photoFile, Authentication authentication,
			RedirectAttributes redirect) {

		try {
			UserDetails user = (UserDetails) authentication.getPrincipal();
			Account account = accountServices.findById(user.getUsername());

			// Update account fields
			account.setFullname(fullname);
			account.setEmail(email);
			account.setPhone(phone);
			account.setAddress(address);

			// Handle photo upload if provided
			if (photoFile != null && !photoFile.isEmpty()) {
				String fileName = ImageUtil.save(photoFile);
				if (fileName != null) {
					// Delete old photo if exists
					if (account.getPhoto() != null) {
						ImageUtil.delete(account.getPhoto());
					}
					account.setPhoto(fileName);
				}
			}

			// Save updated account
			accountServices.save(account);

			redirect.addFlashAttribute("message", "updateSuccess");
		} catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute("message", "updateFail");
		}

		return "redirect:/profile#edit-profile";
	}
}
