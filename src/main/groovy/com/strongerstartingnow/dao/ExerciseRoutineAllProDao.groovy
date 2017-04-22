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
class ExerciseRoutineAllProDao {
	@Autowired
	ExerciseRoutine exerciseRoutine
	
	@Autowired
	ExerciseRoutineAllPro exerciseRoutineAllPro
	
	@Autowired
	UserAccountDao userAccountDao
	
	@Autowired
	DataSource dataSource
	
	boolean saveOrUpdateExerciseRoutineAllPro(ExerciseRoutineAllPro allPro) {
		String sqlQuery = ""
		if(hasExerciseRoutineAllPro(allPro.userAccount)) {
			sqlQuery = "UPDATE exercise_routine_allpro SET cycle = '" + allPro.cycle + "', week = '" + allPro.week + "' WHERE useraccount_username = '" + allPro.userAccount.username + "'"
		} else {
			sqlQuery = "INSERT INTO exercise_routine_allpro(useraccount_username, cycle, week) VALUES ('" + allPro.userAccount.username + "', '" + allPro.cycle + "', '" + allPro.week + "')"
		}

		def sql = new Sql(dataSource)
		def result = sql.executeUpdate(sqlQuery)
		return result > 0
	}
	
	def hasExerciseRoutineAllPro(UserAccount ua) {
		def sql = new Sql(dataSource)
		try {
			def result = sql.firstRow("SELECT * from exercise_routine_allpro where useraccount_username = '" + ua.getUsername() + "'")
			if(result != null) {
				return true;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false
		} finally{
			sql.close()
		}
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
