package com.strongerstartingnow.dao
import groovy.transform.Canonical
import org.springframework.stereotype.Component
import com.strongerstartingnow.validators.ValidUsername
@Canonical
@Component
class UserAccount {
	@ValidUsername
	String username
	def name
	def password
	def email
	boolean enabled = true
}
