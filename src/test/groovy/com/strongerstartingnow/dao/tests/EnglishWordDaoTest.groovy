package com.strongerstartingnow.dao.tests

import static org.junit.Assert.*;

import com.strongerstartingnow.dao.EnglishWordDao
import org.junit.Test
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ActiveProfiles
import org.springframework.beans.factory.annotation.Autowired

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class EnglishWordDaoTest {

	@Autowired
	EnglishWordDao englishWordDao
	
	@Test
	public void test() {
		def englishWord = englishWordDao.getWordByLength(5);
		assert englishWord.getLength() > 0;
	}
	
	@Test
	public void testRandomUsername() {
		int i = 0
		def randomUsername;
		while (i < 10) {
			randomUsername = englishWordDao.getRandomWord().word + englishWordDao.getRandomWord().word
			println "random username is: $randomUsername and i is $i"
			i++
			assert randomUsername.length() > 0
		}
	}
	
/*	@Test
	public void testNoVowel() {
		int i = 0
		while(i < 100) {
			assert "aeoiu".indexOf(englishWordDao.randomConsonant().toString()) == -1
			i++
		}
	}*/

}
