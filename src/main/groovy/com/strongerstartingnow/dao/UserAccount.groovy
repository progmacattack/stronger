package com.strongerstartingnow.dao
import groovy.transform.Canonical

import java.security.Principal
import java.util.Collection;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import com.strongerstartingnow.validators.ValidUsername

@Canonical
@Component
class UserAccount implements Principal, UserDetails {
	@ValidUsername
	String username
	def name
	@Size(min=2, max=30)
	String password
	def email
	boolean enabled = true
	UserAccountRole userAccountRole
	
	public String getName() {
		return username	
	}

	@Override
	public String toString() {
		return "UserAccount [username=" + username + ", name=" + name + ", password=" + password + ", email=" + email
				+ ", enabled=" + enabled + ", userAccountRole=" + userAccountRole + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((userAccountRole == null) ? 0 : userAccountRole.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAccount other = (UserAccount) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (enabled != other.enabled)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (userAccountRole == null) {
			if (other.userAccountRole != null)
				return false;
		} else if (!userAccountRole.equals(other.userAccountRole))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public String getPassword() {
		return password.toString();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		println "in method getAuthorities in UserAccount"
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return enabled;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return enabled;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return enabled;
	}
}
