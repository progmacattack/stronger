package com.strongerstartingnow.dao.tests

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.*
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.strongerstartingnow.dao.UserAccount
import com.strongerstartingnow.dao.UserAccountRole
import com.strongerstartingnow.dao.UserAccountRole.RoleEnum;
import com.strongerstartingnow.dao.UserAccountDao
import com.strongerstartingnow.dao.UserAccountRoleDao
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class UserAccountDaoTest {
	
	@Autowired
	UserAccountDao userAccountDao
	
	@Autowired
	UserAccountRoleDao userAccountRoleDao
	
	UserAccount userAccount1 = new UserAccount(email: "wet@feet.com", username: "2tangerine", password: "hellohello", name: "Mrv Man")
	UserAccount userAccount2 = new UserAccount(email: "potato@leek.com", username: "2keyboard", password: "hellohello", name: "Keyboard Tapper")
	def testUserAccounts = [userAccount1, userAccount2]
	
	@Before
	void init() {
	}
	
	@After
	void finishWith() {
		println "Cleaning up..."
		for (userAccount in testUserAccounts) {
			userAccountRoleDao.delete(userAccount)
			userAccountDao.delete(userAccount)
		}
	}
	
	@Test
	void testAllUsers() {
		println "gonna print some users..."
		userAccountDao.create(userAccount1)
		userAccountDao.create(userAccount2)
		List<UserAccount> allUsers = userAccountDao.getUsers()
		println "all users are: ${allUsers.toString()}"
		assert allUsers.size > 1
	}
	
	@Test
	void testCreate() {
		println "creating a database entry..."
		assertTrue("Should be able to create entry in database", userAccountDao.create(userAccount1))
	}

	@Test
	void testDelete() {
		userAccountDao.create(userAccount1)
		println "deleting a database entry..."
		assertTrue("Should be able to delete an entry in database", userAccountDao.delete(userAccount1))
	}
	
	@Test
	void testGetUserAccount() {
		userAccountDao.create(userAccount1)
		println "getting user account..."
		UserAccount retrievedUser = userAccountDao.getUserAccount(userAccount1.username)
		assertEquals("Retrieved user account email should equal provided", userAccount1.email, retrievedUser.email)
		assertEquals("Retrieved user enabled should equal what was provided", userAccount1.enabled, retrievedUser.enabled)
	}
	
	@Test
	void testGetNonExistingUserAccount() {
		println "getting user account with name that doesn't exist..."
		try {
			UserAccount retrievedUser = userAccountDao.getUserAccount("fakeusername")
		} catch(UsernameNotFoundException ex) {
			assert true; //should throw username not found exception
		}
	}
	
	@Test
	void testIfAccountExists() {
		userAccountDao.create(userAccount1)
		Boolean userExists = userAccountDao.userExists(userAccount1)
		println "userExists is $userExists"
		assertTrue("User account should exist", userExists)
	}
	
	@Test
	void testIfAccountDoesNotExists() {
		UserAccount userAccount = new UserAccount();
		Boolean userExists = userAccountDao.userExists(userAccount)
		assertFalse("User account should not exist", userExists)
	}
	
	@Test
	void testIfUsernameExists() {
		userAccountDao.create(userAccount1)
		Boolean usernameExists = userAccountDao.usernameExists(userAccount1.username)
		println "userExists is $usernameExists"
		assertTrue("Username should exist", usernameExists)
	}
	
	@Test
	void testIfUsernameDoesNotExists() {
		UserAccount userAccount = new UserAccount();
		userAccount.username = "blahblahblahthiscantexist1234567890"
		Boolean usernameExists = userAccountDao.usernameExists(userAccount.username)
		assertFalse("Username should not exist", usernameExists)
	}
	
	@Test
	void testIfUserIsEnabled() {
		userAccountDao.create(userAccount1)
		Boolean userEnabled = userAccountDao.userIsEnabled(userAccount1.username)
		assertTrue("User account is enabled", userEnabled)
	}
	
	@Test
	void testUserRole() {
		userAccountDao.create(userAccount1)
		assertTrue("User is user role", userAccountRoleDao.getUserRole(userAccount1) == (RoleEnum.ROLE_USER));
	}
}
