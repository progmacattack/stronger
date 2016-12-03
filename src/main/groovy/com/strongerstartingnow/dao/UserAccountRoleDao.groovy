package com.strongerstartingnow.dao
import com.mysql.jdbc.PreparedStatement;
import com.strongerstartingnow.dao.UserAccountRole.RoleEnum
import java.sql.ResultSet
import java.sql.SQLException
import groovy.sql.Sql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.stereotype.Component

@Component
class UserAccountRoleDao {
	private JdbcTemplate jdbcTemplate
	
	@Autowired
	UserAccountRoleDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate
	}
	
	UserAccountRole create(UserAccount userAccount, RoleEnum roleEnum) {
		def sql = "insert into useraccount_role(username, role) values (?,?)"
		def params = [userAccount.username, roleEnum.sqlValue] as Object[]
		//update returns number of rows affected
		if(this.jdbcTemplate.update(sql, params) > 0) {
			String sqlStmnt = "select * from useraccount_role where username = ?"
			def param = [userAccount.username] as Object[]
			return this.jdbcTemplate.queryForObject(sqlStmnt, param, new RowMapper<UserAccountRole>() {
				UserAccountRole mapRow(ResultSet rs, int row) throws SQLException {
					UserAccountRole uar = new UserAccountRole([id: rs.getString("id"), username: rs.getString("username"), roleEnum: rs.getString("role") as RoleEnum ])
				}
			});
		} else {
		  return new UserAccountRole();
		};
		
	}
	
	Boolean delete(UserAccount userAccount) {
		def sql = "delete from useraccount_role where username=?"
		def param = [userAccount.username] as Object[]
		println "from roles deleting $param"
		return this.jdbcTemplate.update(sql, param) > 0
	}
	
	RoleEnum getUserRole(UserAccount userAccount) {
		String sql = "select * from useraccount_role where username = ?"
		def param = [userAccount.username] as Object[]
		RoleEnum result = this.jdbcTemplate.queryForObject(sql, param, new RowMapper<RoleEnum>() {
			RoleEnum mapRow(ResultSet rs, int row) throws SQLException {
				RoleEnum result = rs.getString("role");
			}
		})
		return result;
	}
}
