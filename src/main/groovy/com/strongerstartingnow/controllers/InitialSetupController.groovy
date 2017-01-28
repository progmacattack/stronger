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
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.bind.annotation.SessionAttributes

import com.strongerstartingnow.dao.Human
import com.strongerstartingnow.service.InitialSetupService
import com.strongerstartingnow.service.RoutineService

@SessionAttributes("human")
@Controller
public class InitialSetupController {
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	InitialSetupService iss;
	
	@Autowired
	RoutineService rs;

	private Human human;
	
	@ModelAttribute("human")
	public Human humanInfo() {
		Human human = new Human();
		return human;
	}
	
	@RequestMapping(value="/createprofile", method=RequestMethod.POST)
	public String createProfile(@ModelAttribute("human") Human human, BindingResult result) {
		if (result.hasErrors()) {
			return "home";
		} else {			
			iss.setupHuman(human)
			return "routine"
		}
	}
	
	@PostMapping(value="/saveroutine")
	public ModelAndView saveRoutine(@ModelAttribute("human") Human human, Model model, Principal principal) {
		if(principal == null) {
			return new ModelAndView("redirect:/joinus", model)
		}
		rs.saveOrUpdateRoutine(human, principal);				
		return new ModelAndView("saveroutine")
	}
}