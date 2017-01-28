package com.strongerstartingnow.dao

import java.sql.ResultSet
import java.sql.SQLException

import javax.sql.DataSource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

@Component
class HumanDao {
	@Autowired
	UserAccountDao userAccountDao
	
	@Autowired
	DataSource dataSource
	
	private JdbcTemplate jdbcTemplate
	@Autowired
	HumanDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	Boolean create(Human human) {
		String sql = "insert into human (bodyweight_in_pounds, bodyweight_in_kilograms, sex, useraccount_username) values (?,?,?,?)"
		UserAccount ua = human.getUserAccount();
		//try to create user account
		if(userAccountDao.userExists(ua)) {
			try {
				Boolean humanCreated = this.jdbcTemplate
					.update(sql, human.bodyWeightInPounds, human.bodyWeightInKilograms, human.sex.value(), ua.username) > 0;
			} catch(Exception e) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	Boolean retrieve(UserAccount ua) {
		String username = ua.username;
		String sql = "select * from human where useraccount_username = (?)"
		def params = [username] as Object[]
		UserAccount userAccount = new UserAccount()
		try {
			human = (Human) this.jdbcTemplate.quer
			userAccount = (UserAccount)this.jdbcTemplate.queryForObject(sql, params, new RowMapper<UserAccount>() {
				public UserAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
					println "row num is $rowNum"
					userAccount.with {
						name = rs.getString("name")
						email = rs.getString("email")
						username = rs.getString("username")
						password = rs.getString("password")
						enabled = rs.getBoolean("enabled")
					}
					return userAccount;
				}
			})
		} catch(EmptyResultDataAccessException e) {
			e.printStackTrace()
		}
		return userAccount;
	}
}
