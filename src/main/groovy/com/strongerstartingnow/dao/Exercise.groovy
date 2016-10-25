package com.strongerstartingnow.dao

import org.springframework.stereotype.Component
import com.strongerstartingnow.utilities.Convert

@Component
class Exercise {
	def name
	int currentMax
	int currentReps = 8
	int currentWorkingWeight
	
	void setCurrentWorkingWeight(int currentReps) {
		this.currentReps = currentReps
		this.currentWorkingWeight = Convert.maxWeightToThisManyRepsEpleyFormula(this, currentReps)
		this.currentWorkingWeight = 5 * Math.round(this.currentWorkingWeight / 5)
	}
	
	void setCurrentWorkingWeight() throws Exception {
		if(this.currentReps > 0) {
			this.currentWorkingWeight = Convert.maxWeightToThisManyRepsEpleyFormula(this, currentReps)
			this.currentWorkingWeight = 5 * Math.round(this.currentWorkingWeight / 5)
		} else {
			throw Exception
		}
	}
}
