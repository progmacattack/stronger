package com.strongerstartingnow.dao

import groovy.transform.Canonical

import org.springframework.stereotype.Component

@Canonical
@Component
class ExerciseAbility {
	def id
	Exercise exercise
	UserAccount userAccount
	def weightInPounds
	def weightInKilos
	def repeitions
	def sets
	def positionInRoutine
}
