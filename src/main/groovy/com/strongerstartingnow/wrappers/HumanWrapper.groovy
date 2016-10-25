package com.strongerstartingnow.wrappers

import com.strongerstartingnow.constraints.AValidSex;
import com.strongerstartingnow.constraints.AValidWeight;


/**
 * @author Adam Sickmiller
 * A wrapper class to accept input from the web and then when it is clean create an object
 */
class HumanWrapper {
	String weight;
	
	@AValidSex
	def sex;
}
