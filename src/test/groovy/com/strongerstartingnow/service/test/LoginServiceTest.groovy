package com.strongerstartingnow.service.test

import javax.naming.AuthenticationException

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.strongerstartingnow.dao.EnglishWordDao
import com.strongerstartingnow.dao.UserAccount
import com.strongerstartingnow.dao.UserAccountDao
import com.strongerstartingnow.service.LoginService
import com.strongerstartingnow.utilities.EncryptPassword

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class LoginServiceTest {
	@Autowired
	LoginService loginService
	
	@Autowired
	EnglishWordDao englishWordDao
	
	@Autowired
	protected AuthenticationManager authManager;
	
	@Autowired
	EncryptPassword encryptPassword
	
	@Autowired UserAccountDao userAccountDao
	
	UserAccount shmoopy;
	
	@Test
	public void testAutoLoginBadCredentials() {
		try {
			boolean success = loginService
			.autoLoginUser(new UserAccount(username: "this-account-does-not-exist", password: "should-throw-exception"))
		} catch (AuthenticationException aex) {
			println "Caught exception. As expected."
			assert true;
		}
	}
	
	@Test
	public void testAutoLoginValidCredentials() {
		setup();
		try {
			boolean success = loginService.autoLoginUser(shmoopy)
			Assert.assertTrue("User account should exist. Credentials should be valid.", success);
		} catch (AuthenticationException ae) {
			println "Caught exception. Did not expect this."
			println ae.printStackTrace();
			assert false;
		}
	}
	
	@Test
	public void setupRandomUserTest() {
		UserAccount ua = loginService.setupRandomUser();
		Assert.assertNotNull("Should have set up some kind of userAccount")
		Assert.assertTrue("Length of username should be greater than 1 character", ua.username.length() > 1)
		Assert.assertTrue("Length of password should be greater than 1 char and less than 20", ua.password.length() > 1 && ua.password.length() < 20)
	}
	
	@Test
	public void generateRandomUsernameTest() {
			String randomName = loginService.generateRandomUsername();
			Assert.assertTrue("Name should be two words combined, camelCase style", randomName.split("[A-Z]").length == 2)
		
	}
	
	private void setup() {
		shmoopy = new UserAccount(email: "shmoopybaby@something.com", username: "lilShmoops", password: "buttercup", name: "Shmoopy Face")
		boolean deleteIfPresent = userAccountDao.delete(shmoopy)
		boolean created = userAccountDao.create(shmoopy)
		shmoopy.password = "buttercup" //put this back in plain text
		assert created
	}
}
