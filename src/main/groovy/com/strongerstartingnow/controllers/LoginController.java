package com.strongerstartingnow.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.strongerstartingnow.dao.UserAccount;

@Controller
public class LoginController {

	@GetMapping("/login")
	String loginController(Model model) {
		return "login";
	}
}
