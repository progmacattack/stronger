package com.strongerstartingnow.service;

import org.jboss.logging.Logger
import org.springframework.stereotype.Service

import com.strongerstartingnow.dao.Exercise
import com.strongerstartingnow.dao.ExerciseAbility
import com.strongerstartingnow.dao.ExerciseRoutineAllPro
import com.strongerstartingnow.dao.Human
import com.strongerstartingnow.dao.HumanAbilities
import com.strongerstartingnow.dao.HumanAbilities.typicalMaxAsPercentBodyweight
import com.strongerstartingnow.enums.AverageHumanBodyweight
import com.strongerstartingnow.enums.Sex
import com.strongerstartingnow.utilities.Convert
import com.strongerstartingnow.webobjects.SaveRoutineInfo

@Service
class InitialSetupService {
	Logger logger = Logger.getLogger(this.getClass());
	Human human;
	List<ExerciseAbility> defaultAbilities;

	void setupSaveRoutineInfo(SaveRoutineInfo saveRoutineInfo) {
		println "Setting up current routine for ${saveRoutineInfo.human}"
		setupDefaultAbilities(saveRoutineInfo.human)
		setupInitialAllProRoutine(saveRoutineInfo);
		saveRoutineInfo.currentExercises = defaultAbilities;
	}
	
	private void setupInitialAllProRoutine(SaveRoutineInfo sri) {
		ExerciseRoutineAllPro erap = new ExerciseRoutineAllPro([cycle: 1, week: 1]);
		sri.exerciseRoutineAllPro = erap;
	}
	
	private void setupDefaultAbilities(Human human) {
		defaultAbilities = new ArrayList<ExerciseAbility>();
		for(ex in Exercise.DefaultExercises.values()) {
			Exercise exrcse = new Exercise([name: ex.sqlName])
			ExerciseAbility ea = new ExerciseAbility([exercise: exrcse])
			if(human.sex == Sex.FEMALE) {
				if(human.bodyWeightInPounds <= 0) {
					human.bodyWeightInPounds = AverageHumanBodyweight.FemaleInUsa.inPounds()
				}							
				ea.weightInPounds = Convert.maxWeightToWorkingWeight(human.bodyWeightInPounds * ex.getUntrainedFemaleMaxAsPercentBodyweight(), ex.defaultReps);							
			} else {
				if(human.bodyWeightInPounds <= 0) {
					println "Average male weight: " + AverageHumanBodyweight.MaleInUsa.inPounds()
					human.bodyWeightInPounds = AverageHumanBodyweight.MaleInUsa.inPounds()
				}
				ea.weightInPounds = Convert.maxWeightToWorkingWeight(human.bodyWeightInPounds * ex.getUntrainedMaleMaxAsPercentBodyweight(), ex.defaultReps);
			}
			ea.weightInKilos = ea.weightInPounds * Convert.Weight.kilograms.fromPounds();
			
			//round these weights to nearest 5
			ea.weightInPounds = 5 * Math.round(ea.weightInPounds / 5)
			ea.weightInKilos = 5 * Math.round(ea.weightInKilos / 5)	
			ea.sets = ex.defaultSets
			ea.repetitions = ex.defaultReps	
			ea.positionInRoutine = ex.positionInRoutine
			defaultAbilities.add(ea)	
		}
	}
	
	public saveRoutine(Human human) {
		
	}
	
	@Deprecated
	public HumanAbilities guessAllHumanAbilities(Human human) {
		logger.info("Details as first noticed in calculateHumanAbilities: " + human);
		HumanAbilities abilities = new HumanAbilities();
		if (human.sex == Sex.MALE) {
			try {
				human.bodyWeightInPounds as int
			}
			catch(NumberFormatException nfe) {
			}
			
			if (human.bodyWeightInPounds <= 0) {
				human.bodyWeightInPounds = AverageHumanBodyweight.MaleInUsa.inPounds();
			}
			int weight = human.bodyWeightInPounds;
			
			abilities.barbellBenchPress.currentMax = weight * typicalMaxAsPercentBodyweight.barbellBenchPress.untrainedMale
			abilities.highBarbellBackSquat.currentMax = weight * typicalMaxAsPercentBodyweight.highBarbellBackSquat.untrainedMale
			abilities.barbellPendlayRow.currentMax = weight * typicalMaxAsPercentBodyweight.barbellPendlayRow.untrainedMale
			abilities.barbellOverheadPress.currentMax = weight * typicalMaxAsPercentBodyweight.barbellOverheadPress.untrainedMale
			abilities.barbellFrontSquat.currentMax = weight * typicalMaxAsPercentBodyweight.barbellFrontSquat.untrainedMale
			abilities.lowBarbellBackSquat.currentMax = weight * typicalMaxAsPercentBodyweight.lowBarbellBackSquat.untrainedMale
			abilities.machineCurl.currentMax = weight * typicalMaxAsPercentBodyweight.machineCurl.untrainedMale
			abilities.smithMachineCalfRaise.currentMax = weight * typicalMaxAsPercentBodyweight.smithMachineCalfRaise.untrainedMale
			abilities.romanianDeadlift.currentMax = weight * typicalMaxAsPercentBodyweight.romanianDeadlift.untrainedMale
	
		} else if (human.sex == Sex.FEMALE) {
			if (human.bodyWeightInPounds <= 0) {
				human.bodyWeightInPounds = AverageHumanBodyweight.FemaleInUsa.inPounds();
			}
			int weight = human.bodyWeightInPounds;
			
			abilities.barbellBenchPress.currentMax = weight * typicalMaxAsPercentBodyweight.barbellBenchPress.untrainedFemale
			abilities.highBarbellBackSquat.currentMax = weight * typicalMaxAsPercentBodyweight.highBarbellBackSquat.untrainedFemale
			abilities.barbellPendlayRow.currentMax = weight * typicalMaxAsPercentBodyweight.barbellPendlayRow.untrainedFemale
			abilities.barbellOverheadPress.currentMax = weight * typicalMaxAsPercentBodyweight.barbellOverheadPress.untrainedFemale
			abilities.barbellFrontSquat.currentMax = weight * typicalMaxAsPercentBodyweight.barbellFrontSquat.untrainedFemale
			abilities.lowBarbellBackSquat.currentMax = weight * typicalMaxAsPercentBodyweight.lowBarbellBackSquat.untrainedFemale
			abilities.machineCurl.currentMax = weight * typicalMaxAsPercentBodyweight.machineCurl.untrainedFemale
			abilities.smithMachineCalfRaise.currentMax = weight * typicalMaxAsPercentBodyweight.smithMachineCalfRaise.untrainedFemale
			abilities.romanianDeadlift.currentMax = weight * typicalMaxAsPercentBodyweight.romanianDeadlift.untrainedFemale
		}
		
		//round weights to nearest 5
		for(Exercise exercise : abilities.activityList) {
			exercise.currentMax = 5 * Math.round(exercise.currentMax / 5)
			exercise.setCurrentWorkingWeight();
			println "Setting $exercise.name to new max of $exercise.currentMax"
		}
		
		println "barbell bench press current max is $abilities.barbellBenchPress.currentMax"
		
		return abilities;
	}
	
}
