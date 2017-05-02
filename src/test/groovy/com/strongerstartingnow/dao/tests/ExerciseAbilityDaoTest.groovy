package com.strongerstartingnow.dao.tests

import static org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.strongerstartingnow.dao.Exercise
import com.strongerstartingnow.dao.ExerciseAbility
import com.strongerstartingnow.dao.ExerciseAbilityDao
import com.strongerstartingnow.dao.ExerciseDao
import com.strongerstartingnow.dao.UserAccount
import com.strongerstartingnow.dao.UserAccountDao

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class ExerciseAbilityDaoTest {
	
	@Autowired
	ExerciseAbilityDao exerciseAbilityDao
	
	@Autowired
	ExerciseDao exerciseDao
	
	@Autowired
	UserAccountDao userAccountDao
	
	UserAccount user = new UserAccount(email: "potato@leek.com", username: "keyboardd", password: "hellohello", name: "Keyboard Tapper")	
	UserAccount user2 = new UserAccount(email: "wet@feet.com", username: "tangerine", password: "hellohello", name: "Mrv Man")
	String exerciseToCreate = "toothbrush_bouncer"
	
	@Before
	public void beforeTests() {
		Exercise exToDelete = exerciseDao.getExerciseByName(exerciseToCreate)
		print "exToDelete is $exToDelete"
		if(exToDelete != null) {
			exerciseDao.deleteExercise(exToDelete)
			assert exerciseDao.getExerciseByName(exerciseToCreate) == null
		}
		if(userAccountDao.userExists(user)) {
			userAccountDao.delete(user)
		}
		if(userAccountDao.userExists(user2)) {
			userAccountDao.delete(user2)
		}
	}
	@After
	public void afterTests() {
	}
	
	@Test
	public void testCreateReadAndDelete() {
		Exercise exCreated = exerciseDao.createExercise(exerciseToCreate)
		userAccountDao.create(user)
		ExerciseAbility exAb = new ExerciseAbility(
			exercise: exCreated,
			userAccount: user,
			weightInPounds: 100,
			weightInKilos: 100/2.2,
			repetitions: 7,
			sets: 4,
			positionInRoutine: 1
			)
		ExerciseAbility eaReturned = exerciseAbilityDao.createExerciseAbility(exAb)
		println "eaReturned: $eaReturned"
		assert eaReturned != null
		exerciseAbilityDao.deleteExerciseAbility(eaReturned)
		assert null == exerciseAbilityDao.getExerciseAbilityById(eaReturned.id)
		exerciseDao.deleteExercise(exCreated)
		assert null == exerciseDao.getExerciseById(exCreated.id)
		userAccountDao.delete(user)
	
		try {
			userAccountDao.getUserAccount(user.username)
		} catch(UsernameNotFoundException ex) {
			assert true
		}
	}
	
	@Test
	public void testChildRowsAreDeleted() {
		Exercise exCreated = exerciseDao.createExercise(exerciseToCreate)
		userAccountDao.create(user)
		ExerciseAbility exAb = new ExerciseAbility(
			exercise: exCreated,
			userAccount: user,
			weightInPounds: 100,
			weightInKilos: 100/2.2,
			repetitions: 7,
			sets: 4,
			positionInRoutine: 1
			)
		ExerciseAbility eaReturned = exerciseAbilityDao.createExerciseAbility(exAb)
		println "eaReturned: $eaReturned"
		assert eaReturned != null
		exerciseDao.deleteExercise(exCreated)
		assert null == exerciseDao.getExerciseById(exCreated.id)
		userAccountDao.delete(user)		
		try {
			userAccountDao.getUserAccount(user.username)
		} catch(UsernameNotFoundException ex) {
			assert true
		}
	}
	
	@Test
	public void getExAbsForEx() {
		Exercise exCreated = exerciseDao.createExercise(exerciseToCreate)
		userAccountDao.create(user)
		userAccountDao.create(user2)
		ExerciseAbility exAb = new ExerciseAbility(
			exercise: exCreated,
			userAccount: user,
			weightInPounds: 100,
			weightInKilos: 100/2.2,
			repetitions: 7,
			sets: 4,
			positionInRoutine: 1
			)
		ExerciseAbility exAb2 = new ExerciseAbility(
			exercise: exCreated,
			userAccount: user2,
			weightInPounds: 100,
			weightInKilos: 100/2.2,
			repetitions: 7,
			sets: 4,
			positionInRoutine: 1
			)
		ExerciseAbility eaReturned1 = exerciseAbilityDao.createExerciseAbility(exAb)
		ExerciseAbility eaReturned2 = exerciseAbilityDao.createExerciseAbility(exAb2)
		assert exerciseAbilityDao.getExerciseAbilitiesForExercise(exCreated) != null
		
		assert exerciseDao.getExerciseById(exCreated.id) != null
		exerciseDao.deleteExercise(exCreated)
		assert exerciseDao.getExerciseById(exCreated.id) == null 
		
		userAccountDao.delete(user)
		userAccountDao.delete(user2)
	}
	
	@Test
	public void getExAbsForUser() {
		Exercise exCreated = exerciseDao.createExercise(exerciseToCreate)
		userAccountDao.create(user)
		userAccountDao.create(user2)
		ExerciseAbility exAb = new ExerciseAbility(
			exercise: exCreated,
			userAccount: user,
			weightInPounds: 100,
			weightInKilos: 100/2.2,
			repetitions: 7,
			sets: 4,
			positionInRoutine: 1
			)
		ExerciseAbility exAb2 = new ExerciseAbility(
			exercise: exCreated,
			userAccount: user2,
			weightInPounds: 100,
			weightInKilos: 100/2.2,
			repetitions: 7,
			sets: 4,
			positionInRoutine: 1
			)
		ExerciseAbility eaReturned1 = exerciseAbilityDao.createExerciseAbility(exAb)
		ExerciseAbility eaReturned2 = exerciseAbilityDao.createExerciseAbility(exAb2)
		assert exerciseAbilityDao.getExerciseAbilitiesForExercise(exCreated) != null
		
		assert userAccountDao.getUserAccount(user.username).username != null 
		userAccountDao.delete(user)
		try {
			userAccountDao.getUserAccount(user.username)
		} catch(UsernameNotFoundException ex) {
			assert true
		}
		
		assert userAccountDao.getUserAccount(user2.username).username != null 
		userAccountDao.delete(user2)
				try {
			userAccountDao.getUserAccount(user2.username)
		} catch(UsernameNotFoundException ex) {
			assert true
		}
	}

}
