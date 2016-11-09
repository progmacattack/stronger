package com.strongerstartingnow.dao

import com.strongerstartingnow.utilities.EncryptPassword
import java.sql.ResultSet
import java.sql.SQLException

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
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
	
	@Autowired
	EncryptPassword encryptPassword
	
	@Autowired
	UserAccountRoleDao userAccountRoleDao
	
	Boolean create(UserAccount userAccount) {
		String sql = "insert into useraccount (username, name, email, password, enabled) values (?,?,?,?,?)"
		String hashedPass = encryptPassword.encode(userAccount.password)
		userAccount.password = hashedPass
		def params = [userAccount.username, userAccount.name, userAccount.email, userAccount.password, userAccount.enabled]
		
		println "passing in params: $params"
		Boolean created = userAccountRoleDao.create(userAccount, UserAccountRole.RoleEnum.ROLE_USER)
		
		//update returns number of rows affected
		return created && this.jdbcTemplate.update(sql, userAccount.username, userAccount.name, userAccount.email, userAccount.password, userAccount.enabled) > 0
	}
	
	Boolean userExists(UserAccount userAccount) {
		String sql = "select * from useraccount where username = ?"
		def param = [userAccount.username] as Object[]
		boolean result = this.jdbcTemplate.query(sql, param, new ResultSetExtractor<Boolean>() {
			public Boolean extractData(ResultSet rs) throws SQLException {
				boolean result = rs.next()
				return result
			}
		})
		return result;
	}
	
	Boolean usernameExists(String username) {
		String sql = "select * from useraccount where username = ?"
		def param = [username] as Object[]
		boolean result = this.jdbcTemplate.query(sql, param, new ResultSetExtractor<Boolean>() {
			public Boolean extractData(ResultSet rs) throws SQLException {
				boolean result = rs.next()
				return result
			}
		})
		return result;
	}
	
	Boolean delete(UserAccount userAccount) {
		String sql = "delete from useraccount where username = (?)"
		return this.jdbcTemplate.update(sql, userAccount.username) > 0 
	}
	
	UserAccount getUserAccount(String usernameProvided) {
		String sql = "select * from userAccount where username = (?)"
		def params = [usernameProvided] as Object[]
		UserAccount userAccount = new UserAccount()
		try {
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
	
	List<UserAccount> getUsers() {
		println "gonna get some users..."
		List<UserAccount> allUsers = this.jdbcTemplate.query(
			"select * from useraccount",
			new RowMapper<UserAccount>() {
				UserAccount mapRow(ResultSet rs, int RowNum) throws SQLException {
					def userAccount = new UserAccount()
					userAccount.setUsername(rs.getString("username"))
					userAccount.setName(rs.getString("name"))
					userAccount.setEmail(rs.getString("email"))
					userAccount.setPassword(rs.getString("password"))
					userAccount.setEnabled(rs.getBoolean("enabled"))
					println "check it out: ${userAccount.name}"
					return userAccount;
				}
			})
		return allUsers
	}
	
	boolean userIsEnabled(String userName) {
		UserAccount retrievedAccount = getUserAccount(userName)
		return retrievedAccount.enabled
	}
}
