package com.strongerstartingnow.utilities

class StringOrLetterUtilities {
	/** Method to generate a random letter
	 * @return char - a random letter
	 */
	static char randomLetter() {
		Integer random = (Integer)Math.floor(Math.random() * 26)
		String letters = 'abcdefghijklmnopqrstuvwxyz'
		return letters.charAt(random)
	}
	
	/*** Method to generate a string that is a regex that will select words beginning with one or more
	* random letters. Letters might repeat
	* @param letterssNeeded
	* @return String a regex of letters needed
	*/
   static String randomLetterRegex(Integer lettersNeeded) {
	   String letterRegex = "^["
	   for(int i = 0; i < lettersNeeded; i++) {
		   letterRegex += randomLetter().toString()
	   }
	   letterRegex += "].*"
	   return letterRegex
   }
}
