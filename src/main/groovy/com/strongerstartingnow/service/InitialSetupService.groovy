package com.strongerstartingnow.service;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

import com.strongerstartingnow.dao.Exercise
import com.strongerstartingnow.dao.Human;
import com.strongerstartingnow.enums.Sex;
import com.strongerstartingnow.enums.AverageHumanBodyweight;
import com.strongerstartingnow.dao.HumanAbilities
import com.strongerstartingnow.dao.HumanAbilities.typicalMaxAsPercentBodyweight;;

@Service
class InitialSetupService {
	Logger logger = Logger.getLogger(this.getClass());
	Human human;

	Human setupHuman (Human human) {
		def sex = human.sex		
		human.humanAbilities = guessAllHumanAbilities(human);
		logger.info("Prodcessed human be like " + human);
		return human;
	}
	
	public HumanAbilities guessAllHumanAbilities(Human human) {
		logger.info("Details as first noticed in calculateHumanAbilities: " + human);
		HumanAbilities abilities = new HumanAbilities();
		if (human.sex == Sex.MALE) {
			try {
				human.weightInPounds as int
			}
			catch(NumberFormatException nfe) {
			}
			
			if (human.weightInPounds <= 0) {
				human.weightInPounds = AverageHumanBodyweight.MaleInUsa.inPounds();
			}
			int weight = human.weightInPounds;
			
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
			if (human.weightInPounds <= 0) {
				human.weightInPounds = AverageHumanBodyweight.FemaleInUsa.inPounds();
			}
			int weight = human.weightInPounds;
			
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
