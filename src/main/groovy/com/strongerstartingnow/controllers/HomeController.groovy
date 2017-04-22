package com.strongerstartingnow.controllers

import java.security.Principal

import javax.servlet.http.HttpSession

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.SessionAttributes

import com.strongerstartingnow.dao.Human
import com.strongerstartingnow.dao.HumanDao
import com.strongerstartingnow.dao.UserAccount
import com.strongerstartingnow.dao.UserAccountDao
import com.strongerstartingnow.service.RoutineService
import com.strongerstartingnow.webobjects.SaveRoutineInfo

@SessionAttributes("saveRoutineInfo")
@Controller
class HomeController {
	
	@Autowired
	UserAccountDao userAccountDao		
	
	@Autowired
	SaveRoutineInfo saveRoutineInfo
	
	@Autowired
	HumanDao humanDao
	
	@Autowired
	Human human
	
	@Autowired
	RoutineService rs
	
	@ModelAttribute("human")
	public Human human() {
		return new Human();
	}
	
	@ModelAttribute("saveRoutineInfo")
	public SaveRoutineInfo saveRoutineInfo(HttpSession session) {
		Human human = session.getAttribute("saveRoutineInfo")
		if(saveRoutineInfo == null) {
			saveRoutineInfo = new SaveRoutineInfo()
		}
		return saveRoutineInfo
	}
	
	@GetMapping("/")
	String home(@ModelAttribute("saveRoutineInfo") SaveRoutineInfo saveRoutineInfo, Model model, Principal principal) {
		if(principal != null) {
			UserAccount user = userAccountDao.getUserAccount(principal.getName());
			saveRoutineInfo.human = humanDao.retrieve(user);
			if(saveRoutineInfo.human == null) {
				saveRoutineInfo.human = new Human();
			}
			addUserHasRoutineAttribute(model, saveRoutineInfo);
			println saveRoutineInfo.human
		}
		/*List<UserAccount> userList = userAccountDao.getUsers()
		model.addAttribute("userAccounts", userList) */
		model.addAttribute("saveRoutineInfo", saveRoutineInfo)
		return "layout_home"
	}
	
	@GetMapping("/updatenav")
	String updateNav(@ModelAttribute("saveRoutineInfo") SaveRoutineInfo saveRoutineInfo, Model model) {
		addUserHasRoutineAttribute(model, saveRoutineInfo);
		return "fragments/navigation"
	}
	
	private addUserHasRoutineAttribute(Model model, SaveRoutineInfo saveRoutineInfo) {
		model.addAttribute("userHasRoutine", saveRoutineInfo.human == null ? false : rs.userHasRoutine(saveRoutineInfo));
	}
}
