package poly.edu.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalAdvice {
	@ModelAttribute("currentPage")
	public String currentPage(HttpServletRequest request) {
		String uri = request.getRequestURI();

		if (uri.contains("/discount"))
			return "discount";
		if (uri.contains("/account"))
			return "account";
		if (uri.contains("/product"))
			return "product";
		if (uri.contains("/category"))
			return "product";
		if (uri.contains("/register"))
			return "register";
		if (uri.contains("/checkout"))
			return "checkout";

		return "";
	}
}
