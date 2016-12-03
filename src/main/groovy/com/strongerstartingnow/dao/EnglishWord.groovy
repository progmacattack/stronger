package com.strongerstartingnow.dao
import org.springframework.stereotype.Component

import groovy.transform.Canonical
@Component
@Canonical
class EnglishWord implements Serializable {
	String word
	
	Integer getLength() {
		return word.length();
	}
	
}
