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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import poly.edu.models.entities.Category;
import poly.edu.models.services.CategoryServices;
import poly.edu.utils.PaginationUtil;

@Controller
@RequestMapping("/dashboard")
public class CategoryController {

	@Autowired
	private CategoryServices categoryServices;

	@ModelAttribute("category")
	public Category initCategory() {
		return new Category();
	}

	@ModelAttribute("categories")
	public List<Category> getCategories() {
		return categoryServices.findAll();
	}

	@GetMapping("/category")
	public String show(Model model, @RequestParam(required = false) String keyword,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
		List<Category> filtered = categoryServices.filter(keyword);
		List<Category> categories = PaginationUtil.paginate(filtered, page, size);
		int pages = PaginationUtil.getTotalPages(filtered, size);

		model.addAttribute("keyword", keyword);
		model.addAttribute("pages", pages);
		model.addAttribute("page", page);
		model.addAttribute("categories", categories);
		model.addAttribute("currentPage", "category");

		return "category-dashboard";
	}

	@PostMapping("/category/save")
	public String save(RedirectAttributes redirect, @ModelAttribute Category category) {

		try {
			categoryServices.save(category);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			redirect.addFlashAttribute("message", "saveFail");
			return "redirect:/dashboard/category";
		}

		redirect.addFlashAttribute("categories", List.of(category));
		redirect.addFlashAttribute("message", "saveTrue");
		return "redirect:/dashboard/category";
	}

	@GetMapping("/category/confirm-delete/{id}")
	public String confirmDelete(RedirectAttributes redirect, @PathVariable("id") String id) {
		Category category = categoryServices.findById(id);
		redirect.addFlashAttribute("deleteCategory", category);
		redirect.addFlashAttribute("showDeleteModal", true);
		return "redirect:/dashboard/category";
	}

	@GetMapping("/category/delete/{id}")
	public String delete(RedirectAttributes redirect, @PathVariable("id") String id) {

		try {
			categoryServices.delete(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			redirect.addFlashAttribute("message", "deleteFail");
			return "redirect:/dashboard/category";
		}

		redirect.addFlashAttribute("message", "deleteTrue");
		return "redirect:/dashboard/category";
	}

	@GetMapping("/category/edit/{id}")
	public String edit(RedirectAttributes redirect, @PathVariable("id") String id) {
		Category category = categoryServices.findById(id);
		redirect.addFlashAttribute("category", category);
		redirect.addFlashAttribute("showModal", true); // Add this to control modal visibility
		return "redirect:/dashboard/category";
	}

	@GetMapping("/category/reset")
	public String reset() {

		return "redirect:/dashboard/category";
	}
	
	@GetMapping("/category/cancel")
	public String cancel() {
		return "redirect:/dashboard/category";
	}

}
