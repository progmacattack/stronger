package com.strongerstartingnow.service.test

import org.junit.Test
import static org.junit.Assert.*
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.strongerstartingnow.enums.Sex
import com.strongerstartingnow.enums.AverageHumanBodyweight
import com.strongerstartingnow.dao.ExerciseAbility
import com.strongerstartingnow.dao.Human
import com.strongerstartingnow.dao.HumanAbilities
import com.strongerstartingnow.service.InitialSetupService
import com.strongerstartingnow.webobjects.SaveRoutineInfo

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class InitialSetupServiceTest {
	
	@Autowired
	Human human
	
	@Autowired
	HumanAbilities humanAbilities
	
	@Autowired
	InitialSetupService initialSetupService
	
	@Test
	void testSetupSaveRoutineInfo() {
		SaveRoutineInfo saveRoutineInfo = new SaveRoutineInfo();
		saveRoutineInfo.human = new Human(sex: Sex.MALE);
		initialSetupService.setupSaveRoutineInfo(saveRoutineInfo)
		assertEquals("Setup human weight should equal constant for male", (int)AverageHumanBodyweight.MaleInUsa.weightInPounds, saveRoutineInfo.human.bodyWeightInPounds)
		
		SaveRoutineInfo saveRoutineInfoWoman = new SaveRoutineInfo();
		saveRoutineInfoWoman.human = new Human(sex: Sex.FEMALE)
		initialSetupService.setupSaveRoutineInfo(saveRoutineInfoWoman)
		println "woman weight: $saveRoutineInfoWoman.human.bodyWeightInPounds"
		assertTrue("Setup human weight should equal constant for female", saveRoutineInfoWoman.human.bodyWeightInPounds == (int)AverageHumanBodyweight.FemaleInUsa.weightInPounds)
		
		List<ExerciseAbility> routine = saveRoutineInfo.currentExercises;
		assertTrue("Some abilities are set", routine != null)
		
		for(ExerciseAbility ea: routine) {
			assertTrue("Default reps should be 8", ea.repetitions == 8)
			assertTrue("Should have some default weight", ea.weightInPounds > 0);
		}
	}
}
