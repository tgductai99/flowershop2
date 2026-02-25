package poly.edu.controllers;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import poly.edu.models.entities.Category;
import poly.edu.models.entities.Product;
import poly.edu.models.services.CategoryServices;
import poly.edu.models.services.ProductServices;
import poly.edu.utils.ImageUtil;
import poly.edu.utils.PaginationUtil;

@Controller
@RequestMapping("/dashboard")
public class ProductController {

	@Autowired
	private ProductServices productServices;

	@Autowired
	private CategoryServices categoryServices;

	@ModelAttribute("product")
	public Product initProduct() {
		return new Product();
	}

	@ModelAttribute("categories")
	public List<Category> getCategories() {

		return categoryServices.findAll();
	}

	@ModelAttribute("products")
	public List<Product> getProducts() {

		return productServices.findAllDescById();
	}

	@GetMapping("/product")
	public String show(Model model, @RequestParam(required = false) String keyword,
			@RequestParam(required = false) String categoryId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		List<Product> filtered = productServices.filter(keyword, categoryId);
		List<Product> products = PaginationUtil.paginate(filtered, page, size);
		int pages = PaginationUtil.getTotalPages(filtered, size);

		model.addAttribute("categoryId", categoryId);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pages", pages);
		model.addAttribute("page", page);
		model.addAttribute("products", products);
		model.addAttribute("currentPage", "product");

		return "product-dashboard";
	}

	@PostMapping("/product/save")
	public String save(RedirectAttributes redirect, 
			@ModelAttribute Product product,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

		try {
			// Validate required fields
			if (product.getName() == null || product.getName().isBlank()) {
				redirect.addFlashAttribute("message", "saveFail");
				redirect.addFlashAttribute("errorDetails", "Product name is required");
				redirect.addFlashAttribute("product", product);
				return "redirect:/dashboard/product";
			}
			
			if (product.getPrice() == null || product.getPrice() <= 0) {
				redirect.addFlashAttribute("message", "saveFail");
				redirect.addFlashAttribute("errorDetails", "Product price must be greater than 0");
				redirect.addFlashAttribute("product", product);
				return "redirect:/dashboard/product";
			}
			
			if (product.getAmount() == null || product.getAmount() <= 0) {
				redirect.addFlashAttribute("message", "saveFail");
				redirect.addFlashAttribute("errorDetails", "Product amount must be greater than 0");
				redirect.addFlashAttribute("product", product);
				return "redirect:/dashboard/product";
			}
			
			if (product.getCategory() == null || product.getCategory().getId() == null || product.getCategory().getId().isBlank()) {
				redirect.addFlashAttribute("message", "saveFail");
				redirect.addFlashAttribute("errorDetails", "Please select a category");
				redirect.addFlashAttribute("product", product);
				return "redirect:/dashboard/product";
			}
			
			// Handle image upload
			if (imageFile != null && !imageFile.isEmpty()) {
				// New image uploaded
				String fileName = ImageUtil.save(imageFile);
				product.setImage(fileName);
			} else {
				// No new image uploaded
				if (product.getId() != null) {
					// Editing existing product - keep existing image
					Product existingProduct = productServices.findById(product.getId());
					if (existingProduct != null && existingProduct.getImage() != null) {
						product.setImage(existingProduct.getImage());
					} else {
						redirect.addFlashAttribute("message", "saveFail");
						redirect.addFlashAttribute("errorDetails", "Product image is required");
						redirect.addFlashAttribute("product", product);
						return "redirect:/dashboard/product";
					}
				} else {
					// New product without image
					redirect.addFlashAttribute("message", "saveFail");
					redirect.addFlashAttribute("errorDetails", "Product image is required for new products");
					redirect.addFlashAttribute("product", product);
					return "redirect:/dashboard/product";
				}
			}
			
			// Set default values if not provided
			if (product.getCreateDate() == null) {
				product.setCreateDate(java.time.LocalDate.now());
			}
			if (product.getAvailable() == null) {
				product.setAvailable(true);
			}
			
			productServices.save(product);
			redirect.addFlashAttribute("products", List.of(product));
			redirect.addFlashAttribute("message", "saveTrue");
			
		} catch (IOException e) {
			e.printStackTrace();
			redirect.addFlashAttribute("message", "saveFail");
			redirect.addFlashAttribute("errorDetails", "Failed to upload image: " + e.getMessage());
			redirect.addFlashAttribute("product", product);
		} catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute("message", "saveFail");
			String errorMsg = e.getMessage();
			if (errorMsg == null || errorMsg.isEmpty()) {
				errorMsg = "Unknown error occurred. Please check all fields are filled correctly.";
			}
			redirect.addFlashAttribute("errorDetails", errorMsg);
			redirect.addFlashAttribute("product", product);
		}

		return "redirect:/dashboard/product";
	}

	@GetMapping("/product/confirm-delete/{id}")
	public String confirmDelete(RedirectAttributes redirect, @PathVariable("id") Integer id) {
		Product product = productServices.findById(id);
		redirect.addFlashAttribute("deleteProduct", product);
		redirect.addFlashAttribute("showDeleteModal", true);
		return "redirect:/dashboard/product";
	}

	@GetMapping("/product/delete/{id}")
	public String delete(RedirectAttributes redirect, @PathVariable("id") Integer id) {

		try {
			// Check if product can be deleted
			if (!productServices.canDelete(id)) {
				redirect.addFlashAttribute("message", "deleteFailReferenced");
				redirect.addFlashAttribute("errorDetails", "This product cannot be deleted because it is referenced in existing orders or discounts. You can mark it as unavailable instead.");
				return "redirect:/dashboard/product";
			}
			
			Product deleted = productServices.delete(id);
			ImageUtil.delete(deleted.getImage());
			redirect.addFlashAttribute("message", "deleteTrue");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			redirect.addFlashAttribute("message", "deleteFail");
			redirect.addFlashAttribute("errorDetails", e.getMessage());
		}

		return "redirect:/dashboard/product";
	}
	
	@GetMapping("/product/soft-delete/{id}")
	public String softDelete(RedirectAttributes redirect, @PathVariable("id") Integer id) {
		try {
			productServices.softDelete(id);
			redirect.addFlashAttribute("message", "softDeleteTrue");
			redirect.addFlashAttribute("successDetails", "Product has been marked as unavailable.");
		} catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute("message", "softDeleteFail");
			redirect.addFlashAttribute("errorDetails", e.getMessage());
		}
		return "redirect:/dashboard/product";
	}

	@GetMapping("/product/edit/{id}")
	public String edit(RedirectAttributes redirect, @PathVariable("id") Integer id) {

		Product product = productServices.findById(id);
		redirect.addFlashAttribute("product", product);
		redirect.addFlashAttribute("showModal", true); // Add this to control modal visibility

		return "redirect:/dashboard/product";
	}

	@GetMapping("/product/reset")
	public String reset() {
		return "redirect:/dashboard/product";
	}
	
	@GetMapping("/product/cancel")
	public String cancel() {
		return "redirect:/dashboard/product";
	}
}
