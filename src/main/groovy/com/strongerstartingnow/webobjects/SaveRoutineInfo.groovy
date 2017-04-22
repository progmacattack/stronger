package com.strongerstartingnow.webobjects

import org.springframework.stereotype.Component

import com.strongerstartingnow.dao.ExerciseAbility
import com.strongerstartingnow.dao.ExerciseRoutine
import com.strongerstartingnow.dao.ExerciseRoutineAllPro
import com.strongerstartingnow.dao.Human
import com.strongerstartingnow.dao.UserAccount

@Component
class SaveRoutineInfo {
	Human human
	ExerciseRoutine exerciseRoutine
	ExerciseRoutineAllPro exerciseRoutineAllPro
	List<ExerciseAbility> currentExercises
	UserAccount userAccount
}
