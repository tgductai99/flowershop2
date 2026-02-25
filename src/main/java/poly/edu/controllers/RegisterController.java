package poly.edu.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import poly.edu.models.entities.Account;
import poly.edu.models.services.AccountServices;
import poly.edu.utils.ImageUtil;

@Controller
public class RegisterController {

	@Autowired
	private AccountServices accountServices;

	@ModelAttribute("account")
	public Account initAccount() {
		return new Account();
	}

	@GetMapping("/register")
	public String show() {
		return "register";
	}

	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("account") Account account, BindingResult result,
			@RequestParam("photoFile") MultipartFile photoFile) {

		// Check if username already exists
		Account existingAccount = accountServices.findById(account.getUsername());
		if (existingAccount != null) {
			result.rejectValue("username", "error.username", "Username already exists");
		}

		if (result.hasErrors()) {
			return "register";
		}

		if (!photoFile.isEmpty()) {
			try {
				String fileName = ImageUtil.save(photoFile);
				account.setPhoto(fileName);
			} catch (IOException e) {
				result.reject("photo", "Could not upload photo");
				return "register";
			}
		}

		accountServices.save(account);

		return "redirect:/login?registerSuccess";
	}
}