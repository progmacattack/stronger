package com.strongerstartingnow.dao

import java.sql.ResultSet
import java.sql.SQLException

import javax.sql.DataSource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

import com.strongerstartingnow.webobjects.SaveRoutineInfo

@Component
class HumanDao {
	@Autowired
	UserAccountDao userAccountDao
	
	@Autowired
	ExerciseAbilityDao exerciseAbilityDao
	
	@Autowired
	DataSource dataSource
	
	private JdbcTemplate jdbcTemplate
	
	@Autowired
	HumanDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	Boolean createHuman(SaveRoutineInfo saveRoutineInfo) {
		boolean update = false;
		String sql = "insert into human (bodyweight_in_pounds, bodyweight_in_kilograms, sex, useraccount_username) values (?,?,?,?)"
		//see if user already has human
		UserAccount ua = saveRoutineInfo.userAccount
		Human human = saveRoutineInfo.human
		Human humanFromDb = retrieve(ua)
		if(humanFromDb != null) {
			update = true;
			sql = "UPDATE human SET bodyweight_in_pounds = ?, bodyweight_in_kilograms = ?, sex = ? WHERE useraccount_username = ?"
		}
		
		//try to create user account
		if(userAccountDao.userExists(ua)) {
			try {
				Boolean humanCreated = this.jdbcTemplate
					.update(sql, human.bodyWeightInPounds, human.bodyWeightInKilograms, human.sex, ua.username) > 0;
			} catch(Exception e) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	/** Return Human object if there is one for the UserAccount. Otherwise, return null **/
	Human retrieve(UserAccount ua) {
		String username = ua.username;
		String sql = "SELECT * FROM human WHERE useraccount_username = ?"
		def params = [username] as Object[]
		Human human = new Human();
		try {
			human = (Human)jdbcTemplate.queryForObject(sql, params, new RowMapper<Human>() {
				Human mapRow(ResultSet rs, int rowNum) throws SQLException {
					human.with {
						bodyWeightInPounds = Integer.parseInt(rs.getString("bodyweight_in_pounds"))
						bodyWeightInKilograms = Integer.parseInt(rs.getString("bodyweight_in_kilograms"))
						sex = (String)rs.getString("sex")
						userAccount = (UserAccount)userAccountDao.getUserAccount(rs.getString("useraccount_username"))
					}
					return human;
				}
			});	
		} catch(EmptyResultDataAccessException e) {
			e.getMessage();
			return null
		}
		return human;
	}
}
