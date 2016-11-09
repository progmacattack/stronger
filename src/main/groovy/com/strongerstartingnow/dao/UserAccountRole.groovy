package com.strongerstartingnow.dao;

import org.springframework.stereotype.Component;

import groovy.transform.Canonical;

@Canonical
@Component
public class UserAccountRole {
	def id
	String username
	enum RoleEnum {
		ROLE_USER("ROLE_USER"),
		ROLE_ADMIN("ROLE_ADMIN")
		
		String sqlValue
		
		private RoleEnum(String sqlValue) {
			this.sqlValue = sqlValue
		}
	}
}
