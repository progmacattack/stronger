package com.strongerstartingnow.enums

import com.strongerstartingnow.utilities.Convert.Weight

//reference from anywhere to get an average bodyweight
enum AverageHumanBodyweight {
	MaleInUsa (194.7),
	FemaleInUsa (164.7);
	
	private int weightInPounds; //in pounds
	private int  weightInKilograms;
	
	AverageHumanBodyweight(def weightInPounds) {
		this.weightInPounds = weightInPounds
		this.weightInKilograms = weightInPounds * Weight.kilograms.fromPounds()
	}
	
	def inPounds() {
		return weightInPounds;
	}
	
	def inKilograms() {
		return weightInKilograms;
	}
}
