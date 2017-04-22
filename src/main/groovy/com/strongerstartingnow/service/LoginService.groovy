package com.strongerstartingnow.service

import groovy.util.logging.Slf4j

import javax.naming.AuthenticationException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.stereotype.Service

import com.strongerstartingnow.dao.EnglishWordDao
import com.strongerstartingnow.dao.UserAccount
import com.strongerstartingnow.dao.UserAccountDao
import com.strongerstartingnow.utilities.EncryptPassword

@Service
@Slf4j
class LoginService {
	
	@Autowired
	EnglishWordDao englishWordDao
	
	@Autowired 
	protected AuthenticationManager authManager;
	
	@Autowired
	EncryptPassword encryptPassword
	
	@Autowired UserAccountDao userAccountDao
	
	
	/** After user registers an account, automatically login that user
	 * 
	 * @param userAccount of user that we want to login
	 * @return true if successful, otherwise will throw something
	 * @throws AuthenticationException if cannot log in user
	 */
	public boolean autoLoginUser(UserAccount userAccount) throws AuthenticationException {
		UsernamePasswordAuthenticationToken token =
				new UsernamePasswordAuthenticationToken(userAccount.username, userAccount.password);
		
		try {
			Authentication auth = authManager.authenticate(token)
		} catch (BadCredentialsException bc) {
			bc.printStackTrace()
			log.debug("Credentials are not valid")
			throw new AuthenticationException("Failed to authenticate user. Cannot auto-login")
		}
		
		SecurityContextHolder.getContext().setAuthentication(token) //authorize user		
		return true
	}
	
	/** Setup a userAccount with a random username and password
	 * 
 	 * @return newly setup user. Will return password in plain text
	 */
	public UserAccount setupRandomUser() {
		log.debug("Setting up a random user with a random password")
		String randomUsername = generateRandomUsername()
		while(userAccountDao.usernameExists(randomUsername)) { //loop to make sure username is unique
			randomUsername = generateRandomUsername()
		}
		
		String randomPassword = RandomStringUtils.random(8, true, true)
		UserAccount userAccount = new UserAccount(username: randomUsername, password: randomPassword)	
		
		Boolean accountCreated = false
		accountCreated = userAccountDao.create(userAccount)
			
		if(accountCreated) { 
			userAccount.password = randomPassword 	//dao encrypts pass. here we return plain text pass
			return userAccount						//because we are defining the password, we need to
		}											// show it to user
		
		log.info("A random user could not be created. There was a problem")
		return null //some problem creating account
	}
	
	/** Generate a random username from words in the database
	 *
	 * @return the randomly generated username
	 */
	String generateRandomUsername() {
		String randomWordOne = englishWordDao.getRandomWord().getWord()
		String randomWordTwo = englishWordDao.getRandomWord().getWord()
		randomWordTwo = randomWordTwo.substring(0, 1).toUpperCase() + randomWordTwo.substring(1)
		return randomWordOne + randomWordTwo
	}
	
	public boolean setupNewUser(UserAccount u) {
		return userAccountDao.create(u);
	}
}
