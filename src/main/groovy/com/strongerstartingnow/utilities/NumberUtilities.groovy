package com.strongerstartingnow.utilities

class NumberUtilities {
	public NumberUtilities() {
		
	}
	
	static Integer roundToFive(Integer num) {
		return 5 * Math.round(num / 5);
	}
	
	static Integer roundToFive(Double num) {
		return 5 * Math.round(num / 5);
	}
}
