package com.strongerstartingnow.dao
import groovy.transform.ToString

import org.springframework.stereotype.Component

import com.strongerstartingnow.enums.AverageHumanBodyweight
import com.strongerstartingnow.enums.Sex

@ToString
@Component
class Human {
	int weightInPounds
	Sex sex = Sex.FEMALE
	HumanAbilities humanAbilities	
}
