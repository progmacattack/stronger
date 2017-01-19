package com.strongerstartingnow.controllers;

import java.security.Principal
import org.jboss.logging.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import com.strongerstartingnow.dao.Human
import com.strongerstartingnow.service.InitialSetupService
import com.strongerstartingnow.service.RoutineService

@Controller
public class InitialSetupController {
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	InitialSetupService iss;
	
	@Autowired
	RoutineService rs;
/*	
	@ModelAttribute("human")
	public Human newHuman() {
		return new Human();
	}
	*/
	@RequestMapping(value="/createprofile", method=RequestMethod.POST)
	public String createProfile(Human human, Model model, BindingResult result) {
		
		if (result.hasErrors()) {
			return "home";
		} else {
			iss.setupHuman(human)
			model.addAttribute("human", human)
			return "initialprofile"
		}
	}
	
	@PostMapping(value="/saveroutine")
	public String saveRoutine(@ModelAttribute("human") Human human, Model model, Principal principal) {
		if(principal == null) {
			return "error"
		}
		println("principal: " + principal.getName());
		rs.saveOrUpdateRoutine(human, principal);				
		return "saveroutine"
		
	}
}