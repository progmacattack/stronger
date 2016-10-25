package com.strongerstartingnow.utilities.test

import static org.junit.Assert.*;

import com.strongerstartingnow.dao.Exercise;
import com.strongerstartingnow.utilities.Convert;
import org.junit.Test;

class ConvertTest {
	
	@Test
	public void maxWeightToThisManyRepsEpleyFormulaTest() {
		Exercise exercise = new Exercise([name: 'test exercise', currentMax: 100])
		int result = Convert.maxWeightToThisManyRepsEpleyFormula(exercise, 5)
		assert result == 85
	}

}
