package com.strongerstartingnow.dao

import groovy.transform.Canonical
import groovy.transform.Sortable

import org.springframework.stereotype.Component

//this class connects a user account with an exercise routine
@Canonical
@Component
class ExerciseRoutineAllPro {
	def id	
	Integer cycle
	Integer week
	UserAccount userAccount
}
