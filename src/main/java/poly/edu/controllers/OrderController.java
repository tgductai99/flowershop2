package poly.edu.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import poly.edu.models.entities.Order;
import poly.edu.models.services.OrderServices;
import poly.edu.utils.CurrencyUtil;

@Controller
@RequestMapping("/dashboard")
public class OrderController {

	@Autowired
	private OrderServices orderServices;
	
	@Autowired
	private CurrencyUtil currencyUtil;

	@ModelAttribute("orders")
	public List<Order> getOrders() {
		return orderServices.findAllDescById();
	}

	@RequestMapping(value = { "", "/order" })
	public String show(Model model,
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String fromDate,
			@RequestParam(required = false) String toDate) {
		
		// Parse dates
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate fromDateParsed = null;
		LocalDate toDateParsed = null;
		
		if (fromDate != null && !fromDate.isEmpty()) {
			try {
				fromDateParsed = LocalDate.parse(fromDate, formatter);
			} catch (Exception e) {
				// Invalid date format, ignore
			}
		}
		
		if (toDate != null && !toDate.isEmpty()) {
			try {
				toDateParsed = LocalDate.parse(toDate, formatter);
			} catch (Exception e) {
				// Invalid date format, ignore
			}
		}
		
		final LocalDate from = fromDateParsed;
		final LocalDate to = toDateParsed;
		
		// Get all orders
		List<Order> allOrders = orderServices.findAllDescById();
		
		// Filter orders
		List<Order> filtered = allOrders.stream()
			.filter(o -> keyword == null || keyword.isEmpty() || 
				o.getAccount().getFullname().toLowerCase().contains(keyword.toLowerCase()) ||
				o.getAccount().getUsername().toLowerCase().contains(keyword.toLowerCase()) ||
				String.valueOf(o.getId()).contains(keyword))
			.filter(o -> status == null || status.isEmpty() || o.getStatus().equals(status))
			.filter(o -> from == null || !o.getCreateDate().isBefore(from))
			.filter(o -> to == null || !o.getCreateDate().isAfter(to))
			.toList();
		
		model.addAttribute("orders", filtered);
		model.addAttribute("keyword", keyword);
		model.addAttribute("status", status);
		model.addAttribute("fromDate", fromDate);
		model.addAttribute("toDate", toDate);
		model.addAttribute("currentPage", "order");
		model.addAttribute("currencyUtil", currencyUtil);
		
		return "order-dashboard";
	}
	
	@GetMapping("/order/view/{id}")
	public String view(@PathVariable("id") Long id, Model model) {
		Order order = orderServices.findById(id);
		
		if (order == null) {
			return "redirect:/dashboard/order";
		}
		
		Double total = orderServices.calculateOrderTotal(order);
		
		model.addAttribute("order", order);
		model.addAttribute("orderTotal", total);
		model.addAttribute("currentPage", "order");
		model.addAttribute("currencyUtil", currencyUtil);
		
		return "order-view";
	}
	
	@GetMapping("/order/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		Order order = orderServices.findById(id);
		
		if (order == null) {
			return "redirect:/dashboard/order";
		}
		
		model.addAttribute("order", order);
		model.addAttribute("currentPage", "order");
		
		return "order-edit";
	}
	
	@PostMapping("/order/update/{id}")
	public String update(@PathVariable("id") Long id,
			@RequestParam("status") String status,
			RedirectAttributes redirect) {
		
		try {
			orderServices.updateStatus(id, status);
			redirect.addFlashAttribute("message", "updateSuccess");
		} catch (Exception e) {
			redirect.addFlashAttribute("message", "updateFail");
		}
		
		return "redirect:/dashboard/order";
	}
}
