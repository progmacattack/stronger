package com.strongerstartingnow.controllers

import groovy.util.logging.Slf4j

import java.security.Principal

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.servlet.ModelAndView

import com.strongerstartingnow.dao.UserAccount
import com.strongerstartingnow.dao.UserAccountDao
import com.strongerstartingnow.service.RoutineService
import com.strongerstartingnow.webobjects.SaveRoutineInfo

@SessionAttributes("saveRoutineInfo")
@Controller
@Slf4j
class RoutineController {
	@Autowired
	UserAccountDao userAccountDao
	
	@Autowired
	SaveRoutineInfo saveRoutineInfo
	
	@Autowired
	RoutineService rs
	
	@ModelAttribute("saveRoutineInfo")
	public SaveRoutineInfo saveRoutineInfo(HttpSession session) {
		SaveRoutineInfo saveRoutineInfo = session.getAttribute("saveRoutineInfo")
		if(saveRoutineInfo == null) {
			saveRoutineInfo = new SaveRoutineInfo()
		}
		return saveRoutineInfo
	}
	
	@GetMapping("/routine")
	public ModelAndView showRoutine(@ModelAttribute("saveRoutineInfo") SaveRoutineInfo saveRoutineInfo, Model model, Principal principal) {
		if(principal == null || !rs.userHasRoutine(saveRoutineInfo)) {
			return new ModelAndView("redirect:/");
		} else {
			//add human with current routine to model
			UserAccount u = userAccountDao.getUserAccount(principal.getName());
			model.addAttribute("userHasRoutine", saveRoutineInfo.human == null ? false : rs.userHasRoutine(saveRoutineInfo))
			log.info("something needs to be here")
		}
		//model
		return new ModelAndView("layout_routine", model)
	}
	
	@GetMapping(["/routine/nextcycle/{cycleNumber}", "/routine/previouscycle/{cycleNumber}"])
	public ModelAndView showRoutineWeek(HttpServletRequest request, @PathVariable("cycleNumber") String cycleNumber,
		Model model, Principal principal, @ModelAttribute SaveRoutineInfo saveRoutineInfo) {
		
		try {
			int cycleNum = Integer.parseInt(cycleNumber);
			log.info("request: " + request.getServletPath());
			saveRoutineInfo.exerciseRoutineAllPro.cycle = cycleNum;
			rs.updateWeight(saveRoutineInfo, request);
			model.addAttribute("saveRoutineInfo", saveRoutineInfo);
		} catch(NumberFormatException nfe) {
			log.error("Cannot view next cycle or update user information. Cycle number provided is not valid and not a number.")
		}
		
		return new ModelAndView("fragments/routine :: main-routine");
	}
}
	