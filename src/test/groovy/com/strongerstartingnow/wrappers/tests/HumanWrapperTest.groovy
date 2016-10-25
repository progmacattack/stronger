package com.strongerstartingnow.wrappers.tests

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
import com.strongerstartingnow.wrappers.HumanWrapper
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class HumanWrapperTest {
	
	HumanWrapper humanWrapper
	
	@Test
	void testSexValidator() {
		HumanWrapper humanWrapper = new HumanWrapper();
		humanWrapper.setSex('notasex')
		println "human wrapper sex is: $humanWrapper.sex"
		assertFalse("humanWrapper object should not have been created", "notasex".equalsIgnoreCase(humanWrapper.sex));
	}
	
}
