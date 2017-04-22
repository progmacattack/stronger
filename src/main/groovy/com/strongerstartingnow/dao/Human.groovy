package com.strongerstartingnow.dao
import groovy.transform.ToString

import org.springframework.stereotype.Component

import com.strongerstartingnow.enums.AverageHumanBodyweight
import com.strongerstartingnow.enums.Sex

@ToString
@Component
class Human {
	int bodyWeightInPounds
	int bodyWeightInKilograms
	String sex = Sex.FEMALE
	UserAccount userAccount	
}
