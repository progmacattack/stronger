package com.strongerstartingnow.dao

import groovy.sql.Sql

import javax.sql.DataSource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ExerciseAbilityDao {
	@Autowired
	ExerciseAbility exerciseAbility
	
	@Autowired
	ExerciseDao exerciseDao
	
	@Autowired
	UserAccountDao userAccountDao
	
	@Autowired
	DataSource dataSource
	
	private class ExerciseAbilitySqlOps {
		def sqlInsertStatement = "INSERT INTO exercise_ability(useraccount_username, exercise_id, weight_in_pounds, weight_in_kilos, repetitions, sets, position_in_routine) VALUES (:userAccountUsername,:exerciseId,:weightInPounds,:weightInKilos,:repetitions,:sets,:positionInRoutine)"
		def sqlSelectStatement = "SELECT * from exercise_ability where id = ?"
		def sqlDeleteStatement = "DELETE from exercise_ability where id = ?"
		def sqlSelectByExerciseStatement = "SELECT * from exercise_ability where exercise_id = ?"
		def sqlSelectByUserStatement = "SELECT * from exercise_ability where useraccount_username = ?"
		
		public class Params {
			def userAccountUsername
			def exerciseId
			int weightInPounds
			int weightInKilos
			def repetitions
			def sets
			def positionInRoutine
		}
		
		Params getParamsFrom(ExerciseAbility exerciseAbility) {
			return new Params(
				userAccountUsername: exerciseAbility.userAccount.username,
				exerciseId: exerciseAbility.exercise.id,
				weightInPounds: exerciseAbility.weightInPounds,
				weightInKilos: exerciseAbility.weightInKilos,
				repetitions: exerciseAbility.repetitions,
				sets: exerciseAbility.sets,
				positionInRoutine: exerciseAbility.positionInRoutine
			);
		}
		
		/**
		 * take the results of a retrieve query and convert to exercise ability object
		 * @param result the results of an sql query to exercise_ability
		 * @return ExerciseAbility
		 */
	
		ExerciseAbility getExerciseAbilityFromSqlResult(def result) {
			if(result == null) {
				return null
			}
			Exercise exercise = exerciseDao.getExerciseById(result.exercise_id);
			UserAccount userAccount = userAccountDao.getUserAccount(result.useraccount_username);
			ExerciseAbility exerciseAbility = new ExerciseAbility([
				id: result.id,
				userAccount: userAccount,
				exercise: exercise,
				weightInPounds: result.weight_in_pounds,
				weightInKilos: result.weight_in_kilos,
				repetitions: result.repetitions,
				sets: result.sets,
				positionInRoutine: result.position_in_routine
				])
			return exerciseAbility;
		}
	}
	
	List<ExerciseAbility> saveOrUpdateExerciseAbilities(List<ExerciseAbility> eaList, UserAccount user) {		
		def currentEas = getExerciseAbilitiesForUser(user)
		//if user already has exercise ability w same exercise, then delete it before saving new
		currentEas.each { currentEa ->
			eaList.each { newEa ->
				if(currentEa.exercise.id.equals(newEa.exercise.id)) {
					deleteExerciseAbility(currentEa)
				} 
			}
		}
			
		def savedEas = [];
		eaList.each {
			savedEas << createExerciseAbility(it)
		}
		return savedEas
	}
	
	ExerciseAbility createExerciseAbility(ExerciseAbility exerciseAbility) {
		def sql = new Sql(dataSource)
		ExerciseAbilitySqlOps exerciseAbilitySqlOps = new ExerciseAbilitySqlOps();
		println "param (eg) to use for weight in pounds: " + exerciseAbilitySqlOps.getParamsFrom(exerciseAbility).weightInPounds
		def exerciseAbilityId = sql.executeInsert(exerciseAbilitySqlOps.sqlInsertStatement, exerciseAbilitySqlOps.getParamsFrom(exerciseAbility));
		println "id of create ea is $exerciseAbilityId"
		return getExerciseAbilityById(exerciseAbilityId[0][0])
	}
	
	ExerciseAbility getExerciseAbilityById(def exerciseAbilityId) {
		def sql = new Sql(dataSource)
		ExerciseAbilitySqlOps exerciseAbilitySqlOpts = new ExerciseAbilitySqlOps()
		def result = sql.firstRow(exerciseAbilitySqlOpts.sqlSelectStatement, exerciseAbilityId);
		println "ea result is $result"
		return exerciseAbilitySqlOpts.getExerciseAbilityFromSqlResult(result);
	}
	
	List<ExerciseAbility> getExerciseAbilitiesForExercise(Exercise exercise) {
		def sql = new Sql(dataSource)
		ExerciseAbilitySqlOps exerciseAbilitySqlOpts = new ExerciseAbilitySqlOps()
		List<ExerciseAbility> resultList = new ArrayList<>()
		def rows = sql.rows(exerciseAbilitySqlOpts.sqlSelectByExerciseStatement, exercise.id)
		rows.forEach {
			ExerciseAbility ea = exerciseAbilitySqlOpts.getExerciseAbilityFromSqlResult(it)
			println "found ea for ex: $ea"
			resultList.add(ea)
		}
		return resultList
	} 
	
	List<ExerciseAbility> getRoutine(UserAccount user) {
		return getExerciseAbilitiesForUser(user)
	}
	
	List<ExerciseAbility> getExerciseAbilitiesForUser(UserAccount user) {
		def sql = new Sql(dataSource)
		ExerciseAbilitySqlOps exerciseAbilitySqlOpts = new ExerciseAbilitySqlOps()
		List<ExerciseAbility> resultList = new ArrayList<>()
		def rows = sql.rows(exerciseAbilitySqlOpts.sqlSelectByUserStatement, user.username)
		rows.forEach {
			ExerciseAbility ea = exerciseAbilitySqlOpts.getExerciseAbilityFromSqlResult(it)
			println "found ea for user: $ea"
			resultList.add(ea)
		}
		return resultList
	}
	
	Boolean deleteExerciseAbility(ExerciseAbility exerciseAbility) {
		def sql = new Sql(dataSource)
		ExerciseAbilitySqlOps exerciseAbilitySqlOpts = new ExerciseAbilitySqlOps()
		
		try {
			sql.execute(exerciseAbilitySqlOpts.sqlDeleteStatement, exerciseAbility.id)
		} catch (Exception e) {
			return false //failed request
		}
		
		return true //successful request
	}
	
}
