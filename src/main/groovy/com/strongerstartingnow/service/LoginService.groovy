package com.strongerstartingnow.service

import com.strongerstartingnow.dao.EnglishWord
import com.strongerstartingnow.dao.EnglishWordDao
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

@Service
class LoginService {
	
	@Autowired
	EnglishWordDao englishWordDao
	
	String generateRandomUsername() {
		String randomWordOne = englishWordDao.getRandomWord().getWord()
		String randomWordTwo = englishWordDao.getRandomWord().getWord()
		randomWordTwo = randomWordTwo.substring(0, 1).toUpperCase() + randomWordTwo.substring(1)
		return randomWordOne + randomWordTwo
	}
}
