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
class ExerciseRoutineDao {
	@Autowired
	ExerciseRoutine exerciseRoutine
	
	@Autowired
	UserAccountDao userAccountDao
	
	@Autowired
	DataSource dataSource
	
	ExerciseRoutine createExerciseRoutine(String erName, UserAccount ua) {
		def sql = new Sql(dataSource)
		def routineId = sql.executeInsert "INSERT INTO exercise_routine(useraccount_username, routine_name) VALUES ('" + ua.username + "', '" + erName + "')"
		return getExerciseRoutineById(routineId.flatten().getAt(0), ua)
	}
	
	def getExerciseRoutineByUsername(String username) {
		UserAccount ua = userAccountDao.getUserAccount(username);
		Set resultSet = []
		def sql = new Sql(dataSource)
		def result = sql.eachRow("SELECT * from exercise_routine where useraccount_username = '" + username + "'") { row ->
			resultSet << new ExerciseRoutine([id: row.id, userAccount: ua, routineName: row.routine_name])
			println "found result: $row"
		}
		return resultSet
	}
	
	ExerciseRoutine getExerciseRoutineById(def routineId, UserAccount ua) {
		def sql = new Sql(dataSource)
		def result = sql.firstRow("SELECT * from exercise_routine where id = ?", routineId.toString())
		if (result != null && ua.username.equals(result.useraccount_username)) {
			return new ExerciseRoutine([id: result.id, userAccount: ua, routineName: result.routine_name])
		} else
			return null
	}
	
}
