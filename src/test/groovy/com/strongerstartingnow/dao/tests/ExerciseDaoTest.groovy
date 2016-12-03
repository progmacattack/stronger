package com.strongerstartingnow.dao.tests

import static org.junit.Assert.*

import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.strongerstartingnow.dao.Exercise
import com.strongerstartingnow.dao.ExerciseDao

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class ExerciseDaoTest {
	
	@Autowired
	ExerciseDao exerciseDao
	
	@After
	public void afterTests() {
	}
	
	@Test
	public void testCreateReadAndDelete() {
		Exercise ex = exerciseDao.createExercise("heavy barr");
		assert exerciseDao.getExerciseById(ex.id) != null; 
		assert exerciseDao.getExerciseByName(ex.name).name.contentEquals("heavy barr")
		exerciseDao.deleteExercise(ex);
		assert exerciseDao.getExerciseById(ex.id) == null; 
		assert ex.name.equals("heavy barr");
	}

}
