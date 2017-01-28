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
	void testSetupHuman() {
		Human humanMan = initialSetupService.setupHuman(new Human(sex: Sex.MALE))
		assertTrue("Setup human weight should equal constant for male", humanMan.bodyWeightInPounds == (int)AverageHumanBodyweight.MaleInUsa.weightInPounds)
		
		Human humanWoman = initialSetupService.setupHuman(new Human(sex: Sex.FEMALE))
		assertTrue("Setup human weight should equal constant for female", humanWoman.bodyWeightInPounds == (int)AverageHumanBodyweight.FemaleInUsa.weightInPounds)
		
		List<ExerciseAbility> routine = humanMan.currentRoutine
		assertTrue("Some abilities are set", routine != null)
		
		for(ExerciseAbility ea: routine) {
			assertTrue("Default reps should be 8", ea.repetitions == 8)
			assertTrue("Should have some default weight", ea.weightInPounds > 0);
		}
	}
}
