package com.strongerstartingnow.dao

import groovy.transform.Canonical
import groovy.transform.Sortable

import org.springframework.stereotype.Component

//this class connects a user account with an exercise routine
@Canonical
@Component
class ExerciseRoutine {
	String routineName
	def id
	UserAccount userAccount
}
