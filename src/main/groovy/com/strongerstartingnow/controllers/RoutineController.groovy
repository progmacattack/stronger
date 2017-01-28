package com.strongerstartingnow.controllers

import groovy.util.logging.Slf4j

import java.security.Principal

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
@Slf4j
class RoutineController {
	@GetMapping("/routine")
	public String showRoutine(Model model, Principal principal) {
		if(principal == null) {
			return "/";
		}
		model
		return "routine";
	}
}
