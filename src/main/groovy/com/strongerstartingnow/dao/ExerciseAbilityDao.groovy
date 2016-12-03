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
	DataSource dataSource
	
	private class ExerciseAbilityParams {
		Exercise exercise
		UserAccount userAccount
		def weightInPounds
		def weightInKilos
		def repeitions
		def sets
		def positionInRoutine
	}
	
	Exercise createExercise(String exerciseName) {
		def sql = new Sql(dataSource)
		ExerciseAbilityParams params = new ExerciseAbilityParams();
		//TODO set all these parameters.
		def sqlStatement = "INSERT INTO exercise_ability(useraccount_username, exercise_id, weight_in_pounds, weight_in_kilos, repetitions, sets, position_in_routine) VALUES (?,?,?,?,?,?,?)"
		
		def exerciseId = sql.executeInsert(sqlStatement, params);
		def rowInfo = getExerciseById(exerciseId.flatten().getAt(0))
		return new Exercise([id: rowInfo.id, name: rowInfo.name])
	}
	
	def deleteExercise(Exercise exercise) {
		def sql = new Sql(dataSource)
		sql.execute "DELETE FROM exercise WHERE name = '" + exercise.name + "'"
	}
	
	def getExerciseByName(def exerciseName) {
		def sql = new Sql(dataSource)
		def result = sql.firstRow("SELECT * from exercise where name = ?", exerciseName.toString())
		return result
	}
	
	def getExerciseById(def exerciseId) {
		def sql = new Sql(dataSource)
		def result = sql.firstRow("SELECT * from exercise where id = ?", exerciseId.toString())
		return result
	}
	
}
