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
			Exercise ex = exerciseDao.getExerciseByName(it.exercise.name)
			if(ex != null) {
				it.setExercise(ex)
			} else {
				log.warn("Attempted to add non-existing exercise. Be sure exercises that might be added are in DB")
			}
		}
		exerciseAbilityDao.saveOrUpdateExerciseAbilities(eaList, u);
	}
}
