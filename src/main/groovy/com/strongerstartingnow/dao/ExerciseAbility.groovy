package com.strongerstartingnow.dao

import groovy.transform.Canonical
import groovy.transform.Sortable

import org.springframework.stereotype.Component

//this class describes one human's ability with one exercise
@Canonical
@Component
@Sortable(includes = ['positionInRoutine'])
class ExerciseAbility {
	Integer positionInRoutine
	def id
	Exercise exercise
	UserAccount userAccount
	int weightInPounds
	int weightInKilos
	def repetitions
	def sets
}
