package com.strongerstartingnow.controllers;

import org.jboss.logging.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import com.strongerstartingnow.dao.Exercise
import com.strongerstartingnow.dao.Human
import com.strongerstartingnow.dao.HumanAbilities
import com.strongerstartingnow.enums.Sex;
import com.strongerstartingnow.service.InitialSetupService

@Controller
public class InitialSetupController {
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	InitialSetupService iss;
	
	@RequestMapping(value="/createprofile", method=RequestMethod.POST)
	public String createProfile(Model model, Human human, BindingResult result) {
		
		if (result.hasErrors()) {
			return "home";
		} else {
			Human setupHuman = iss.setupHuman(human)
			logger.info("Setup human as follows: " + human + " in " + this.getClass())
			logger.info("Human abilities are as follows..." + human.humanAbilities + "...adding to model")
			for(Exercise exercise : human.humanAbilities.activityList) {
				logger.info(exercise.name + " has max weight of " + exercise.currentMax);
			}
			model.addAttribute("humanAbilities", human.humanAbilities)
			for(Exercise ex : human.humanAbilities.activityList) {
				println "reps are $ex.currentReps"
			}
			return "initialprofile"
		}
	}
}
