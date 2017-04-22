package com.strongerstartingnow.controllers;

import java.security.Principal
import javax.servlet.http.HttpSession

import org.jboss.logging.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.servlet.ModelAndView

import com.strongerstartingnow.dao.Human
import com.strongerstartingnow.dao.UserAccount
import com.strongerstartingnow.service.InitialSetupService
import com.strongerstartingnow.service.RoutineService
import com.strongerstartingnow.webobjects.SaveRoutineInfo

@SessionAttributes("saveRoutineInfo")
@Controller
public class InitialSetupController {
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	InitialSetupService iss;
	
	@Autowired
	RoutineService rs;

	private SaveRoutineInfo saveRoutineInfo;
	
	@ModelAttribute("saveRoutineInfo")
	public SaveRoutineInfo saveRoutineInfo(HttpSession session) {
		SaveRoutineInfo saveRoutineInfo = session.getAttribute("saveRoutineInfo")
		if(saveRoutineInfo == null) {
			saveRoutineInfo = new SaveRoutineInfo()
		}
		return saveRoutineInfo
	}
	
	@ModelAttribute("userAccount")
	public UserAccount userAccount() {
		UserAccount ua = new UserAccount();
		return ua;
	}
	
	@ModelAttribute("newRoutine")
	public boolean newRoutine() {
		return true;
	}
	
	@RequestMapping(value="/createprofile", method=RequestMethod.POST)
	public String createProfile(@ModelAttribute("saveRoutineInfo") SaveRoutineInfo saveRoutineInfo, @ModelAttribute("human") Human human, BindingResult result) {
		if (result.hasErrors()) {
			return "layout_home";
		} else {
			saveRoutineInfo.human = human			
			iss.setupSaveRoutineInfo(saveRoutineInfo)
			return "layout_routine"
		}
	}
	
	@PostMapping(value="/saveroutine")
	public ModelAndView saveRoutine(@ModelAttribute("saveRoutineInfo") SaveRoutineInfo saveRoutineInfo, Model model, Principal principal) {
		if(principal == null) {
			return new ModelAndView("joinus :: createaccount")
		}
		rs.saveOrUpdateRoutine(saveRoutineInfo, principal);				
		return new ModelAndView("saveroutine :: saved")
	}
}