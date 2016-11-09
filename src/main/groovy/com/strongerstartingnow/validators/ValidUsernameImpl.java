package com.strongerstartingnow.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongerstartingnow.dao.UserAccountDao;


public class ValidUsernameImpl implements ConstraintValidator<ValidUsername, String>{

	@Autowired
	UserAccountDao userAccountDao;

	private int min;
	private int max;
	
	@Override
	public void initialize(ValidUsername constraintAnnotation) {
		min = constraintAnnotation.min();
		max = constraintAnnotation.max();
	}

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		if (username.length() < min) {
			return false;
		}
		
		if (username.length() > max) {
			return false;
		}
		
		if (userAccountDao.usernameExists(username)) {
			return false;
		}
		
		return true;
	}

}
