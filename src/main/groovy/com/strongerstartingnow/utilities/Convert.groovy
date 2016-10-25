package com.strongerstartingnow.utilities;

import com.strongerstartingnow.dao.Exercise

class Convert {
	//an enum of different units of weight relative to pounds. use to convert from pounds to other units of weight, like kilos
	enum Weight {
		pounds (1),
		kilograms (0.453592);
		
		private final double relativeToPounds;
		
		Weight(double relativeToPounds) {
			this.relativeToPounds = relativeToPounds;
		}
		
		double toKilograms() {
			return relativeToPounds;
		}
	}
	
	
	/**
	 * @param exercise that we want to convert. should have a currentMax field that is non null
	 * @param reps number of repetitions
	 * @return int of the weight that should be lifted for the given number of reps
	 */
	static int maxWeightToThisManyRepsEpleyFormula(Exercise exercise, int reps) {
		int result = exercise.currentMax / (1 + reps / 30)
		return result
	}
}
