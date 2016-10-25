package com.strongerstartingnow.enums

import com.strongerstartingnow.utilities.Convert.Weight

//reference from anywhere to get an average bodyweight
enum AverageHumanBodyweight {
	MaleInUsa (194.7),
	FemaleInUsa (164.7);
	
	private def weightInPounds; //in pounds
	private def  weightInKilograms;
	
	AverageHumanBodyweight(def weightInPounds) {
		this.weightInPounds = weightInPounds
		this.weightInKilograms = weightInPounds * Weight.pounds.toKilograms()
	}
	
	def inPounds() {
		return weightInPounds;
	}
	
	def inKilograms() {
		return weightInKilograms;
	}
}
