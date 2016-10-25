package com.strongerstartingnow.dao

import java.sql.ResultSet
import java.sql.SQLException

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.stereotype.Component

@Component
class UserAccountDao {
	
	private JdbcTemplate jdbcTemplate
	 
	@Autowired
	UserAccountDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	Boolean create(UserAccount userAccount) {
		String sql = "insert into useraccount (username, name, email, password) values (?,?,?,?)"
		def params = [userAccount.username, userAccount.name, userAccount.email, userAccount.password]
		println "passing in params: $params"
		//update returns number of rows affected
		return this.jdbcTemplate.update(sql, userAccount.username, userAccount.name, userAccount.email, userAccount.password) > 0
	}
	
	Boolean delete(UserAccount userAccount) {
		String sql = "delete from useraccount where username = (?)"
		println "deleting $userAccount"
		return this.jdbcTemplate.update(sql, userAccount.username) > 0 
	}
	
	UserAccount getUserAccount(String username) {
		String sql = "select * from userAccount where username = (?)"
		def params = [username] as Object[]
		UserAccount userAccount = (UserAccount)this.jdbcTemplate.queryForObject(sql, params, new RowMapper<UserAccount>() {
			public UserAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserAccount userAccount = new UserAccount(id: rs.getInt("id"), name: rs.getString("name"), email: rs.getString("email"),\
											username: rs.getString("username"), password: rs.getString("password"))
				return userAccount;
			}
		});
		
		return userAccount;
	}
	
	List<UserAccount> getUsers() {
		println "gonna get some users..."
		List<UserAccount> allUsers = this.jdbcTemplate.query(
			"select * from useraccount",
			new RowMapper<UserAccount>() {
				UserAccount mapRow(ResultSet rs, int RowNum) throws SQLException {
					def userAccount = new UserAccount()
					userAccount.setId(rs.getInt("id"))
					userAccount.setUsername(rs.getString("username"))
					userAccount.setName(rs.getString("name"))
					userAccount.setEmail(rs.getString("email"))
					userAccount.setPassword(rs.getString("password"))
					println "check it out: ${userAccount.name}"
					return userAccount;
				}
			})
	}
}
