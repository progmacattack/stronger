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
		Human human = initialSetupService.setupHuman(new Human(sex: Sex.MALE))
		assertTrue("Setup human weight should equal constant for male", human.weightInPounds == (int)AverageHumanBodyweight.MaleInUsa.weightInPounds)
		
		HumanAbilities humanAbilities = human.humanAbilities
		assertTrue("Some abilities are set", humanAbilities != null)
		assertTrue("Default reps should be 8", humanAbilities.barbellBenchPress.currentReps == 8)
		
		humanAbilities.barbellBenchPress.setCurrentWorkingWeight();
		assertTrue("Current working weight should be less than max", humanAbilities.barbellBenchPress.currentWorkingWeight < humanAbilities.barbellBenchPress.currentMax);
		
		Human humanWoman = initialSetupService.setupHuman(new Human(sex: Sex.FEMALE))
		assertTrue("Setup human weight should equal constant for female", humanWoman.weightInPounds == (int)AverageHumanBodyweight.FemaleInUsa.weightInPounds)	
		
	}
}
