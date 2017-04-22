package com.strongerstartingnow.service

import groovy.util.logging.Slf4j

import java.security.Principal

import javax.servlet.http.HttpServletRequest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.strongerstartingnow.dao.Exercise
import com.strongerstartingnow.dao.ExerciseAbility
import com.strongerstartingnow.dao.ExerciseAbilityDao
import com.strongerstartingnow.dao.ExerciseDao
import com.strongerstartingnow.dao.ExerciseRoutineAllPro
import com.strongerstartingnow.dao.ExerciseRoutineAllProDao
import com.strongerstartingnow.dao.ExerciseRoutineDao
import com.strongerstartingnow.dao.Human
import com.strongerstartingnow.dao.HumanDao
import com.strongerstartingnow.dao.UserAccount
import com.strongerstartingnow.dao.UserAccountDao
import com.strongerstartingnow.enums.ExerciseRoutine
import com.strongerstartingnow.utilities.Convert
import com.strongerstartingnow.utilities.NumberUtilities
import com.strongerstartingnow.webobjects.SaveRoutineInfo
@Slf4j
@Service
class RoutineService {
	@Autowired
	UserAccountDao userAccountDao
	
	@Autowired
	HumanDao humanDao
	
	@Autowired
	ExerciseRoutineDao exerciseRoutineDao
	
	@Autowired
	ExerciseAbilityDao exerciseAbilityDao
	
	@Autowired
	ExerciseRoutineAllProDao exerciseRoutineAllProDao
	
	@Autowired
	ExerciseDao exerciseDao
	
	boolean saveOrUpdateRoutine(SaveRoutineInfo saveRoutineInfo, Principal principal) {
		String username = principal.getName()
		log.info("saving routine for: " + username)
		UserAccount u = userAccountDao.getUserAccount(username);
		List<ExerciseAbility> eaList = saveRoutineInfo.currentExercises
		eaList.each {			
			it.setUserAccount(u);
			if(it.weightInKilos == 0) {
				it.weightInKilos = it.weightInPounds * Convert.Weight.kilograms.fromPounds();
				it.weightInKilos = 5 * Math.round(it.weightInKilos / 5)
			}
			if(it.weightInPounds == 0) {
				it.weightInPounds = it.weightInKilos / Convert.Weight.kilograms.fromPounds();
				it.weightInPounds = 5 * Math.round(it.weightInPounds / 5)
			}
			Exercise ex = exerciseDao.getExerciseByName(it.exercise.name)
			if(ex != null) {
				it.setExercise(ex)
			} else {
				log.warn("Attempted to add non-existing exercise. Be sure exercises that might be added are in DB")
			}
		}
		exerciseAbilityDao.saveOrUpdateExerciseAbilities(eaList, u);
		saveRoutineInfo.userAccount = u;
		saveRoutineInfo.exerciseRoutineAllPro.userAccount = u;
		humanDao.createHuman(saveRoutineInfo);
		
		boolean uHasRoutine = userHasThisRoutineAlready(u, ExerciseRoutine.ALLPRO.name());
		if(!uHasRoutine) {
			println "user does not have this routine"
			exerciseRoutineDao.createExerciseRoutine(ExerciseRoutine.ALLPRO.name(), u)
		} else {
			println "user has this routine"
		}
		
		exerciseRoutineAllProDao.saveOrUpdateExerciseRoutineAllPro(saveRoutineInfo.exerciseRoutineAllPro);
	}
	
	boolean userHasThisRoutineAlready(UserAccount u, String routineNm) {
		Set routines = exerciseRoutineDao.getExerciseRoutineByUsername(u.username)
		int hasIt = 0;
		routines.each {
			println "routine name: ${it.routineName}"
			if(it.routineName.equals(routineNm)) {
				hasIt++
			}
		}
		return hasIt > 0
	}
	
	public void updateWeight(SaveRoutineInfo saveRoutineInfo, HttpServletRequest request) {
		Double multiplier = request.getServletPath().contains("nextcycle") ? 1.1 : 0.9;
		for(ExerciseAbility ex : saveRoutineInfo.currentExercises) {
			ex.weightInPounds = NumberUtilities.roundToFive(ex.weightInPounds * multiplier);			
		}
	}
	
	public boolean userHasRoutine(SaveRoutineInfo saveRoutineInfo) {
		return saveRoutineInfo.currentExercises != null || saveRoutineInfo.userAccount != null;
	}
	
}
