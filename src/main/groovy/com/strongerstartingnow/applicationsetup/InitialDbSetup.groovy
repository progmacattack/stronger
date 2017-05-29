package com.strongerstartingnow.applicationsetup

import groovy.util.logging.Slf4j

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

import com.strongerstartingnow.dao.Exercise
import com.strongerstartingnow.dao.ExerciseDao
import com.strongerstartingnow.dao.UserAccount
import com.strongerstartingnow.dao.UserAccountDao
import com.strongerstartingnow.dao.UserAccountRoleDao
import com.strongerstartingnow.dao.UserAccountRole.RoleEnum

//this class executes once after startup
@Component
@Slf4j
class InitialDbSetup implements CommandLineRunner{
	
	@Autowired
	ExerciseDao exerciseDao
	
	@Autowired
	UserAccountDao userAccountDao
	
	@Autowired
	UserAccountRoleDao userAccountRoleDao
	
	@Override
	public void run(String... arg0) throws Exception {
		log.info("Running initial database setup...")
		insertInitialExercises();
		insertTestUser();
		insertBlogUser();
	}
	
	private void insertInitialExercises() {
		log.info('Inserting initial exercises into db')	
		List<Exercise> exercisesAdded = new ArrayList<>()
		List<Exercise> exercisesThereAlready = new ArrayList<>()
		Exercise.DefaultExercises.values().each {
			Exercise exCheck = exerciseDao.getExerciseByName(it.getSqlName())
			if(exCheck == null) {
				exercisesAdded.add(exerciseDao.createExercise(it.getSqlName()))
			} else {
				exercisesThereAlready.add(exCheck)
			}
		}
		exercisesAdded.each {
			log.debug("Successfully added exercise '" + it.getName() + "' to db")
		}
		exercisesThereAlready.each {
			log.debug("Exercise '" + it.getName() + "' already in db")
		}
		
		if(exercisesAdded.size() > 0) {
			log.info("Initial exercises inserted successfully")
		} else if(exercisesThereAlready.size() > 0) {
			log.info("Exercises already in database")
		} else {
			log.info("One or more of exercises attempted to add at startup failed. Please check data")
		}	
	}
	
	//insert a test user into the database
	private void insertTestUser() {
		UserAccount u = new UserAccount([username: 'testuser1234', password: 'testpassword', email: 'testuser@1234.com', name: 'John Bubba Prog IV', enabled: 'true'])
		log.info("verifying test user " + u.username + " / " + u.password)
		if(userAccountDao.userExists(u)) {
			log.info("test user found in db")
		} else {
			log.info("test user not found in db, entering...")
			userAccountDao.create(u)
		}
	}

	//insert an blog user into the database
	private void insertBlogUser() {
		UserAccount u = new UserAccount([username: 'blogposter', password: 'blogpostman', email: 'testuser@1234.com', name: 'Account that can post to blog', enabled: 'true'])
		log.info("verifying blog user " + u.username + " / " + u.password)
		if(userAccountDao.userExists(u)) {
			log.info("blog user found in db")
		} else {
			log.info("blog user not found in db, entering...")
			userAccountDao.create(u)
			if(userAccountRoleDao.update(u, RoleEnum.ROLE_ADMIN)) {
				log.info("blog user role changed to ROLE_ADMIN")
			} else {
				log.info("Could not update blog user role.")
			}
		}
	}

}
