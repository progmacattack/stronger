package com.strongerstartingnow.controllers

import javax.validation.Valid

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

import com.strongerstartingnow.dao.Human
import com.strongerstartingnow.dao.UserAccount
import com.strongerstartingnow.dao.UserAccountDao
import com.strongerstartingnow.service.LoginService

@Controller
class JoinUsController {
	
	@Autowired
	UserAccountDao userAccountDao
	
	@Autowired
	UserAccount userAccount
	
	@Autowired
	Human human
	
	@Autowired
	LoginService loginService

	@GetMapping("/joinus")
	String joinus(Model model) {
		UserAccount userAccount = new UserAccount()
		model.addAttribute("userAccount", userAccount)
		model.addAttribute("randomUsername", loginService.generateRandomUsername())
		return "joinus"
	}
	
	@PostMapping("/joinus")
	String checkUsernameAndJoin(@Valid UserAccount userAccount, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "joinus"
		}
		println "u: " + userAccount.username + ", p: " + userAccount.password
		
		Boolean accountCreated = false;
		accountCreated = userAccountDao.create(userAccount);
		
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
