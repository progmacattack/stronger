package com.strongerstartingnow.utilities

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder

@Component
class EncryptPassword implements PasswordEncoder {
	
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();	
	
	@Override
	public String encode(CharSequence pass) {
		String encodedPass = encoder.encode(pass);
		return encodedPass;
	}

	@Override
	public boolean matches(CharSequence providedPassword, String hashedPassword) {
		if (encoder.matches(providedPassword, hashedPassword)) {
			return true;
		}
		return false;
	}

}
