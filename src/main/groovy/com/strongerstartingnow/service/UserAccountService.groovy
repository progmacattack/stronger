package com.strongerstartingnow.service
import com.strongerstartingnow.dao.UserAccountDao
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
class UserAccountService {
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserAccountDao userAccountDao;
	
}
