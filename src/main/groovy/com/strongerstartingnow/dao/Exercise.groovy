package com.strongerstartingnow.dao

import groovy.transform.Canonical

import org.springframework.stereotype.Component

import com.strongerstartingnow.utilities.Convert

//this class describes the nature of an exercise and is not specific to one user.
@Component
@Canonical
class Exercise {
	def id
	def name
	int currentMax
	int currentReps = 8
	int currentWorkingWeight
	int currentSets = 2
	
	enum DefaultExercises {		
		frontSquat("front squat",0.525, 0.3375, 2, 8, 1),
		barbellBenchPress("barbell bench press",0.66, 0.5, 2, 8, 2),
		pendlayRow("pendlay row",0.45, 0.3, 2, 8, 3),
		smithMachineCalfRaise("smith machine calf raise",0.45, 0.35, 2, 8, 4),
		barbellOverheadPress("barbell overhead press",0.4, 0.25, 2, 8, 5),
		machineCurl("machine curl",0.33, 0.25, 2, 8, 6),
		romanianDeadlift("romanian deadlift",0.4, 0.3, 2, 8, 7);
		
		double untrainedMaleMaxAsPercentBodyweight
		double untrainedFemaleMaxAsPercentBodyweight
		int defaultSets
		int defaultReps
		int positionInRoutine
		String sqlName
		
		DefaultExercises(String sqlName, double untrainedMaleMaxAsPercentBodyweight, double untrainedFemaleMaxAsPercentBodyweight,
			 int defaultSets, int defaultReps, int positionInRoutine) {
			this.sqlName = sqlName
			this.untrainedMaleMaxAsPercentBodyweight = untrainedMaleMaxAsPercentBodyweight
			this.untrainedFemaleMaxAsPercentBodyweight = untrainedFemaleMaxAsPercentBodyweight
			this.defaultSets = defaultSets
			this.defaultReps = defaultReps
			this.positionInRoutine = positionInRoutine
		}
		
		String getSqlName() {
			sqlName
		}
		
		double getUntrainedMaleMaxAsPercentBodyweight() {
			untrainedMaleMaxAsPercentBodyweight
		}
		
		double getUntrainedFemaleMaxAsPercentBodyweight() {
			untrainedFemaleMaxAsPercentBodyweight
		}
	}
	
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
