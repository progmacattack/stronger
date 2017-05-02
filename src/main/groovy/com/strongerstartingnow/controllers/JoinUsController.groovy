package com.strongerstartingnow.controllers

import groovy.util.logging.Slf4j

import javax.servlet.http.HttpSession
import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.SessionAttributes

import com.strongerstartingnow.dao.Human
import com.strongerstartingnow.dao.UserAccount
import com.strongerstartingnow.service.LoginService
import com.strongerstartingnow.service.RoutineService
import com.strongerstartingnow.webobjects.SaveRoutineInfo

@SessionAttributes("saveRoutineInfo")
@Controller
@Slf4j
class JoinUsController {
	@Autowired
	RoutineService rs
	
	@Autowired
	LoginService loginService
	
	@Autowired
	UserAccount userAccount
	
	@Autowired
	Human human

	@ModelAttribute("human")
	public Human getHuman(HttpSession session) {
		Human human = session.getAttribute("human")
		if(human == null) {
			human = new Human()
		}
		return human
	}
	
	@ModelAttribute("saveRoutineInfo")
	public SaveRoutineInfo saveRoutineInfo(HttpSession session) {
		SaveRoutineInfo saveRoutineInfo = session.getAttribute("saveRoutineInfo")
		if(saveRoutineInfo == null) {
			saveRoutineInfo = new SaveRoutineInfo()
		}
		return saveRoutineInfo
	}
		
	@GetMapping("/joinus")
	String joinus(Model model) {
		UserAccount userAccount = new UserAccount()
		human = model["human"]
		
		if(human != null) {
			println "Have human info: " + human.bodyWeightInPounds
		} else {
			println "No human info"
		}
		
		model.addAttribute("userAccount", userAccount)
		model.addAttribute("randomUsername", loginService.generateRandomUsername())
		return "joinus"
	}
	
	@PostMapping("/joinus")
	String checkUsernameAndJoin(@ModelAttribute("userAccount") @Valid UserAccount userAccount,
								BindingResult bindingResult, @ModelAttribute("saveRoutineInfo") SaveRoutineInfo saveRoutineInfo,
								Model model) {	
		if(bindingResult.hasErrors()) {
			return "joinus::createaccount"
		}		
		
		String plainPassword = userAccount.password
		
		boolean accountCreated = false;
		accountCreated = loginService.setupNewUser(userAccount);
		
		if(human != null) {			
			rs.saveOrUpdateRoutine(saveRoutineInfo, userAccount)
		}
		
		def credentials = [username: userAccount.username, password: null];
		model.addAttribute("credentials", credentials);
		
		if(accountCreated) {
			userAccount.password = plainPassword //set pw as plain text to allow auto-login
			loginService.autoLoginUser(userAccount);
			return "youvejoined"
		}
		
		return "error"
	}
	
	@GetMapping("/joinus/random")
	String assignRandomUsernamePasswordAndLogin(Model model, @ModelAttribute Human human) {
		UserAccount ua = loginService.setupRandomUser();
		def credentials = [username: ua.username, password: ua.password]
		model.addAttribute("credentials", credentials) //we are willing to display password here
													   //because we invented it. user does not know it		
		log.debug("Pass: " + ua)
		if(ua != null) {
			loginService.autoLoginUser(ua);
			return "youvejoined :: random"
		} else {
			log.info("There was a problem setting up the random username with random password")
			return "error"
		}
	}
}
