package com.strongerstartingnow

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import com.strongerstartingnow.dao.UserAccount
import com.strongerstartingnow.dao.UserAccountDao

//this class is necessary so that we can have auto-login after registration
@Service
class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserAccountDao userAccountDao	
	
	private UserAccount ua;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			ua = userAccountDao.getUserAccount(username)
		} catch (UsernameNotFoundException ex) {
			throw new UsernameNotFoundException("Could not find username '" + username + "'")
		}
		return ua;
	}

}
