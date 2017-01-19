package com.strongerstartingnow.dao

import groovy.sql.Sql
import java.sql.ResultSet
import java.sql.SQLClientInfoException;
import java.sql.SQLException
import javax.sql.DataSource
import org.springframework.stereotype.Component

import com.strongerstartingnow.utilities.StringOrLetterUtilities
import com.strongerstartingnow.utilities.ListUtilities
import com.strongerstartingnow.dao.Exercise

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor

@Component
class ExerciseDao {
	@Autowired
	Exercise exercise
	
	@Autowired
	ExerciseAbilityDao exerciseAbilityDao
	
	@Autowired
	DataSource dataSource
	
	Exercise createExercise(String exerciseName) {
		def sql = new Sql(dataSource)
		def exerciseId = sql.executeInsert "INSERT INTO exercise(name) VALUES ('" + exerciseName + "')"
		return getExerciseById(exerciseId.flatten().getAt(0))
	}
	
	def deleteExercise(Exercise exercise) {
		def sql = new Sql(dataSource)
		if(exerciseAbilityDao.getExerciseAbilitiesForExercise(exercise) != null) {
			exerciseAbilityDao.getExerciseAbilitiesForExercise(exercise).forEach {
				exerciseAbilityDao.deleteExerciseAbility(it)
			}
		}
		sql.execute "DELETE FROM exercise WHERE name = '" + exercise.name + "'"
	}			
	
	Exercise getExerciseByName(def exerciseName) {
		def sql = new Sql(dataSource)
		def result = sql.firstRow("SELECT * from exercise where name = ?", exerciseName.toString())
		if (result != null) {
			return new Exercise([id: result.id, name: result.name])
		} else
			return null
	}
	
	Exercise getExerciseById(def exerciseId) {
		def sql = new Sql(dataSource)
		def result = sql.firstRow("SELECT * from exercise where id = ?", exerciseId.toString())
		if (result != null) {
			return new Exercise([id: result.id, name: result.name])
		} else
			return null
	}
	
}
