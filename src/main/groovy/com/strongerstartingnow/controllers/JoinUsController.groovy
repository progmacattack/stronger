package com.strongerstartingnow.controllers

import groovy.util.logging.Slf4j

import javax.servlet.http.HttpSession
import javax.validation.Valid

import org.apache.commons.lang3.RandomStringUtils
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
import com.strongerstartingnow.dao.UserAccountDao
import com.strongerstartingnow.service.LoginService
import com.strongerstartingnow.service.RoutineService

@SessionAttributes("human")
@Controller
@Slf4j
class JoinUsController {
	
	@Autowired
	UserAccountDao userAccountDao
	
	@Autowired
	RoutineService rs
	
	@Autowired
	UserAccount userAccount
	
	@Autowired
	Human human
	
	@Autowired
	LoginService loginService

	@ModelAttribute("human")
	public Human getHuman(HttpSession session) {
		Human human = session.getAttribute("human")
		if(human == null) {
			human = new Human()
		}
		return human
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
	String checkUsernameAndJoin(@ModelAttribute("userAccount") @Valid UserAccount userAccount, BindingResult bindingResult, Model model) {	
		if(bindingResult.hasErrors()) {
			return "joinus"
		}
		println "u: " + userAccount.username + ", p: " + userAccount.password
		
		Boolean accountCreated = false;
		accountCreated = userAccountDao.create(userAccount);
		
		Human human = model["human"]
		if(human != null) {
			log.info("User " + userAccount.username + " has prepared routine, saving...")
			rs.saveOrUpdateRoutine(human, userAccount)
			log.info("Human weight: " + human.bodyWeightInPounds)
		}
		
		def credentials = [username: userAccount.username, password: null];
		model.addAttribute("credentials", credentials);
		
		if(accountCreated) {
			return "youvejoined"
		} else {
			return "error"
		}
	}
	
	@GetMapping("/joinus/random")
	String assignRandomUsernamePasswordAndLogin(Model model) {
		String randomUsername = loginService.generateRandomUsername()
		while(userAccountDao.usernameExists(randomUsername)) { //loop to make sure username is unique
			randomUsername = loginService.generateRandomUsername()
		}
		UserAccount userAccount = new UserAccount()
		userAccount.setUsername(randomUsername)
		String randomPassword = RandomStringUtils.random(8, true, true)
		def credentials = [username: randomUsername, password: randomPassword]
		model.addAttribute("credentials", credentials)
		userAccount.setPassword(randomPassword)
		Boolean accountCreated = false
		accountCreated = userAccountDao.create(userAccount)
		
		if(accountCreated) {
			return "youvejoined"
		} else {
			return "error"
		}
	}
}
