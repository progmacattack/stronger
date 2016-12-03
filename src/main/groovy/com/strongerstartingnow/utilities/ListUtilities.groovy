package com.strongerstartingnow.utilities

import com.strongerstartingnow.dao.EnglishWord

class ListUtilities {
	/** Randomly return a member of a list
	 * @param List<Object> a list of something
	 * @return one from the list of the type specified
	 */
	 static def getOneFromList(List<?> listObject) {
		 Integer random = (Integer)Math.floor(Math.random() * listObject.size())
		 if(listObject.size() > 0) {
			 return listObject.get(random)
		 }
		 return null
	 }
}
