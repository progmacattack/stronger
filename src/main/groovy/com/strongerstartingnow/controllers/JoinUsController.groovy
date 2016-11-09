package com.strongerstartingnow.controllers

import javax.validation.Valid;
import javax.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping

import com.strongerstartingnow.dao.Human
import com.strongerstartingnow.dao.UserAccount
import com.strongerstartingnow.dao.UserAccountDao
import com.strongerstartingnow.session.ActiveUserStore

@Controller
class JoinUsController {
	
	@Autowired
	UserAccountDao userAccountDao
	
	@Autowired
	UserAccount userAccount
	
	@Autowired
	Human human
	
	@Autowired
	HttpSession httpSession 
	
	@Autowired
	ActiveUserStore activeUserStore
	
	@GetMapping("/joinus")
	String joinus(Model model) {
		UserAccount userAccount = new UserAccount()
		model.addAttribute("userAccount", userAccount)
		model.addAttribute("currentUsers", activeUserStore.users)
		return "joinus"
	}
	
	@PostMapping("/joinus")
	String checkUsernameAndJoin(@Valid UserAccount userAccount, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "joinus"
		}	
		println "u: " + userAccount.username + ", p: " + userAccount.password
		userAccountDao.create(userAccount);
		model.addAttribute("currentUsers", activeUserStore.users);
		return "youvejoined"
	}
}
