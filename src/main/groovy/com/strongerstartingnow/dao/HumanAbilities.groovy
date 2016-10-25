package com.strongerstartingnow.dao

import groovy.transform.ToString

import org.springframework.stereotype.Component

/**
 * @author Adam Sickmiller
 * A class describing the abilities of a particular human. This is separate from human characteristics like
 * bodyweight or eye color.
 * 
 * New exercises should be added to this class. Don't forget to add an enum with calculations related to each
 *
 */

@ToString
@Component
class HumanAbilities {
	Exercise lowBarbellBackSquat = Exercise.newInstance([name: "Lowbar Back Squat"])
	Exercise highBarbellBackSquat = Exercise.newInstance([name: "Back Squat"]) 
	Exercise barbellFrontSquat = Exercise.newInstance([name: "Front Squat"])
	Exercise barbellBenchPress = Exercise.newInstance([name: "Bench Press"])
	Exercise barbellOverheadPress = Exercise.newInstance([name: "Overhead Press"])
	Exercise barbellPendlayRow = Exercise.newInstance([name: "Pendlay Row"])
	Exercise machineCurl = Exercise.newInstance([name: "Machine Curl"])
	Exercise smithMachineCalfRaise = Exercise.newInstance([name: "Smith Machine Calf Raise"])
	Exercise romanianDeadlift = Exercise.newInstance([name: "Romanian Deadlift"])
	
	def activityList = [barbellFrontSquat, barbellBenchPress, barbellOverheadPress, barbellPendlayRow, machineCurl, smithMachineCalfRaise, romanianDeadlift]
	
	//the general ability of the human
	enum generalAbility {
		untrained,
		novice,
		intermediate,
		advanced
	}
	
	//enum describing typical max ability for different exercises. should have one for every exercise
	 enum typicalMaxAsPercentBodyweight {
		lowBarbellBackSquat (0.735, 0.4725),
		highBarbellBackSquat (0.7, 0.45),
		barbellFrontSquat (0.525, 0.3375),
		barbellBenchPress (0.66, 0.5),
		barbellOverheadPress (0.4, 0.25),
		barbellPendlayRow (0.45, 0.3),
		machineCurl (0.33, 0.25),
		smithMachineCalfRaise (0.45, 0.35),
		romanianDeadlift (0.4, 0.3);
		
		private final double untrainedMale;
		private final double untrainedFemale;
		
		typicalMaxAsPercentBodyweight(double untrainedMale, double untrainedFemale) {
			this.untrainedMale = untrainedMale;
			this.untrainedFemale = untrainedFemale;
		}
		
		double untrainedMale() {
			return untrainedMale;
		}
		
		double untrainedFemale() {
			return untrainedFemale;
		}
	}
}
