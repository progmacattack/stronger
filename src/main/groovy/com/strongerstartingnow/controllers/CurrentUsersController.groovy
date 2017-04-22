package com.strongerstartingnow.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

import com.strongerstartingnow.dao.Human
import com.strongerstartingnow.dao.UserAccount
import com.strongerstartingnow.dao.UserAccountDao

@Controller
class CurrentUsersController {
	@Autowired
	UserAccountDao userAccountDao
	
	@Autowired
	Human human
	
	@GetMapping("/currentusers")
	String home(Model model) {
		List<UserAccount> userList = userAccountDao.getUsers()
		model.addAttribute("userAccounts", userList)
		model.addAttribute("human", human)
		return "layout_currentusers"
	}
}
