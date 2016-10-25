package com.strongerstartingnow.dao.tests

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.strongerstartingnow.dao.UserAccount
import com.strongerstartingnow.dao.UserAccountDao
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class UserAccountDaoTest {
	
	@Autowired
	UserAccountDao userAccountDao
	
	UserAccount userAccount1 = new UserAccount(email: "wet@feet.com", username: "tangerine", password: "hellohello", name: "Mrv Man")
	UserAccount userAccount2 = new UserAccount(email: "potato@leek.com", username: "keyboard", password: "hellohello", name: "Keyboard Tapper")
	def testUserAccounts = [userAccount1, userAccount2]
	
	@Before
	void init() {
	}
	
	@After
	void finishWith() {
		for (userAccount in testUserAccounts) {
			userAccountDao.delete(userAccount)
		}
	}
	
	@Test
	void testAllUsers() {
		println "gonna print some users..."
		List<UserAccount> allUsers = userAccountDao.getUsers()
		println "all users are: ${allUsers.toString()}"
		assert allUsers[0].name == "Timothy Burke"
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
	}
}
