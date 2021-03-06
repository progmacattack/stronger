package com.strongerstartingnow.dao

import groovy.util.logging.Slf4j

import java.sql.ResultSet
import java.sql.SQLException

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

import com.strongerstartingnow.utilities.EncryptPassword

@Slf4j
@Component
class UserAccountDao {
	
	@Autowired
	ExerciseAbilityDao exerciseAbilityDao
	
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
				
		//try to create user account
		try {
			Boolean userAccountCreated = this.jdbcTemplate
				.update(sql, userAccount.username, userAccount.name, userAccount.email, userAccount.password,
					userAccount.enabled) > 0;
		} catch(Exception e) {
			//delete role if there was a problem creating user account and return false
			println "problem creating user account $userAccount.username"
			println e.printStackTrace()
			userAccountRoleDao.delete(userAccount);
			return false;
		}
		
		UserAccountRole createdUserAccountRole = userAccountRoleDao.create(userAccount, UserAccountRole.RoleEnum.ROLE_USER)
		println "created role is: " + createdUserAccountRole.toString();
		userAccount.userAccountRole = createdUserAccountRole;
		
		//returns true if user has been created
		return true;
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
		Boolean success = false
		if(exerciseAbilityDao.getExerciseAbilitiesForUser(userAccount) != null) {
			exerciseAbilityDao.getExerciseAbilitiesForUser(userAccount).forEach {
				success = exerciseAbilityDao.deleteExerciseAbility(it)
			}
		}
		String sql = "delete from useraccount where username = (?)"
		Boolean userDeleted = this.jdbcTemplate.update(sql, userAccount.username) > 0
		Boolean roleDeleted = (userAccountRoleDao.getUserRole(userAccount) == null)

		success = userDeleted && roleDeleted

		return success
	}
	
	UserAccount getUserAccount(String usernameProvided) throws UsernameNotFoundException {
		String sql = "select * from useraccount where username = (?)"
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
			log.debug("Caught an EmptyResultDataAccessException. Printing stracktrace. Will now throw a UsernameNotFoundException")
			throw new UsernameNotFoundException("Could not find username " + usernameProvided)
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
					println "check it out: ${userAccount.username}"
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
