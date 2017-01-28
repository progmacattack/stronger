package com.strongerstartingnow.service

import groovy.util.logging.Slf4j

import java.security.Principal

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.strongerstartingnow.dao.Exercise
import com.strongerstartingnow.dao.ExerciseAbility
import com.strongerstartingnow.dao.ExerciseAbilityDao
import com.strongerstartingnow.dao.ExerciseDao
import com.strongerstartingnow.dao.Human
import com.strongerstartingnow.dao.UserAccount
import com.strongerstartingnow.dao.UserAccountDao
import com.strongerstartingnow.utilities.Convert;
@Slf4j
@Service
class RoutineService {
	@Autowired
	UserAccountDao userAccountDao
	
	@Autowired
	ExerciseAbilityDao exerciseAbilityDao
	
	@Autowired
	ExerciseDao exerciseDao
	
	boolean saveOrUpdateRoutine(Human human, Principal principal) {
		String username = principal.getName()
		log.info("saving routine for: " + username)
		UserAccount u = userAccountDao.getUserAccount(username);
		List<ExerciseAbility> eaList = human.getCurrentRoutine();
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
	}
	
	Human findHumanFromUserAccount(UserAccount userAccount) {
		
	} 
}
