package poly.edu.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import poly.edu.models.entities.Category;
import poly.edu.models.entities.Discount;
import poly.edu.models.entities.Product;
import poly.edu.models.services.CategoryServices;
import poly.edu.models.services.DiscountServices;
import poly.edu.models.services.ProductServices;
import poly.edu.utils.PaginationUtil;

@Controller
@RequestMapping("/dashboard")
@SessionAttributes({ "discount", "product", "discountType", "active", "categoryKeyword", "discountKeyword",
		"categoryId", "discountTable", "productTable", "discountPage", "discountSize", "discountPages", "productPage",
		"productSize", "productPages" })
public class DiscountController {

	@Autowired
	private DiscountServices discountServices;

	@Autowired
	private ProductServices productServices;

	@Autowired
	private CategoryServices categoryServices;

	@ModelAttribute("product")
	public Product initProduct() {
		return new Product();
	}

	@ModelAttribute("discount")
	public Discount initDiscount() {
		return new Discount();
	}

	@ModelAttribute("discountTable")
	public List<Discount> getDiscounts() {
		return discountServices.findAll();
	}

	@ModelAttribute("productTable")
	public List<Product> getProductTable() {
		return productServices.findAll();
	}

	@ModelAttribute("products")
	public List<Product> getProducts() {
		return productServices.findAll();
	}

	@ModelAttribute("categories")
	public List<Category> getCategories() {
		return categoryServices.findAll();
	}

	@GetMapping("/discount")
	public String show(Model model, @RequestParam(required = false) String discountKeyword,
			@RequestParam(required = false) String discountType, @RequestParam(required = false) Boolean active,
			@RequestParam(defaultValue = "0") int discountPage, @RequestParam(defaultValue = "5") int discountSize,
			@RequestParam(required = false) String productKeyword, @RequestParam(required = false) String categoryId,
			@RequestParam(defaultValue = "0") int productPage, @RequestParam(defaultValue = "5") int productSize) {

		List<Product> filteredProducts = productServices.filter(productKeyword, categoryId);
		List<Product> products = PaginationUtil.paginate(filteredProducts, productPage, productSize);
		int productPages = PaginationUtil.getTotalPages(filteredProducts, productSize);

		List<Discount> filteredDiscounts = discountServices.filter(discountKeyword, discountType, active);
		List<Discount> discounts = PaginationUtil.paginate(filteredDiscounts, discountPage, discountSize);
		int discountPages = PaginationUtil.getTotalPages(filteredDiscounts, discountSize);

		model.addAttribute("discountKeyword", discountKeyword);
		model.addAttribute("discountType", discountType);
		model.addAttribute("active", active);
		model.addAttribute("discountPage", discountPage);
		model.addAttribute("discountSize", discountSize);
		model.addAttribute("discountPages", discountPages);
		model.addAttribute("discounts", discounts);

		model.addAttribute("productKeyword", productKeyword);
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("productPage", productPage);
		model.addAttribute("productSize", productSize);
		model.addAttribute("productPages", productPages);
		model.addAttribute("products", products);
		model.addAttribute("currentPage", "discount");

		return "discount-dashboard";
	}

	@PostMapping("/discount/save")
	public String save(RedirectAttributes redirect, Model model, @ModelAttribute("discount") Discount discount,
			@RequestParam(required = false) Integer productId) {

		try {
			Product product = productServices.findById(productId);
			discount.setProduct(product);
			discountServices.save(discount);
			model.addAttribute("discountTable", List.of(discount));
			model.addAttribute("active", "");
			model.addAttribute("discountType", "");
			model.addAttribute("active", "");
			redirect.addFlashAttribute("message", "saveTrue");
		} catch (Exception e) {
			// TODO: handle exception
			redirect.addFlashAttribute("message", "saveFail");
			e.printStackTrace();
			return "redirect:/dashboard/discount";
		}

		return "redirect:/dashboard/discount";
	}

	@GetMapping("/discount/delete/{id}")
	public String delete(RedirectAttributes redirect, @PathVariable("id") Integer id) {

		try {
			discountServices.delete(id);
			redirect.addFlashAttribute("message", "deleteTrue");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			redirect.addFlashAttribute("message", "deleteFail");
			return "redirect:/dashboard/discount/reset";
		}

		return "redirect:/dashboard/discount/reset";
	}

	@GetMapping("/discount/edit/{id}")
	public String edit(Model model, @PathVariable("id") Integer id) {

		Discount discount = discountServices.findById(id);
		model.addAttribute("discount", discount);
		model.addAttribute("product", discount.getProduct());
		return "discount-dashboard";
	}

	@GetMapping("/discount/choose/{id}")
	public String choose(Model model, @PathVariable("id") Integer productId) {

		Product product = productServices.findById(productId);
		model.addAttribute("product", product);
		return "discount-dashboard";
	}

	@GetMapping("/discount/reset")
	public String reset(SessionStatus status) {
		status.setComplete();
		return "redirect:/dashboard/discount";
	}
}
