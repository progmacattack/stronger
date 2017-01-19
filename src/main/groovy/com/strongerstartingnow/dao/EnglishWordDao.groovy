package com.strongerstartingnow.dao
import groovy.sql.Sql
import java.sql.ResultSet
import java.sql.SQLClientInfoException;
import java.sql.SQLException
import javax.sql.DataSource
import org.springframework.stereotype.Component

import com.strongerstartingnow.utilities.StringOrLetterUtilities
import com.strongerstartingnow.utilities.ListUtilities

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor

@Component
class EnglishWordDao {
	@Autowired
	EnglishWord englishWord;
	
	@Autowired
	DataSource dataSource;

	class Params {
		Integer length
		String regex
	}
	
	/** Method to get a random english word. Common words include 10000 common english words
	 * @return EnglishWord object representing word
	 */
	EnglishWord getRandomWord() {
		Params params = new Params()
		params.setRegex(StringOrLetterUtilities.randomLetterRegex(2))
		String regex = params.regex
		String sqlToUse = "select word from commonenglishwords where word regexp ?.regex limit 450"
		def sql = new Sql(dataSource)
		def rows = sql.rows(sqlToUse, params)
		def list = new ArrayList<EnglishWord>()
		rows.forEach {
			list.add(it)
		}
		def word = ListUtilities.getOneFromList(list)
		return word
	}
	
	/** Method to get a random word based on length
	 * Universe of random words is common english words
	 * @param neededLength, e.g. 5 would return a word five letters long
	 * @return
	 */
	EnglishWord getWordByLength(Integer neededLength) {
		Params params = new Params();
		params.setLength(neededLength);
		params.setRegex("^[" + StringOrLetterUtilities.randomLetter().toString() + StringOrLetterUtilities.randomLetter().toString() + "].*")
		String sqlToUse = "select word from commonenglishwords where length(word) = :length and word regexp :regex limit 450"
		def sql = new Sql(dataSource)
		def rows = sql.rows sqlToUse, params
		def list = new ArrayList<EnglishWord>()
		rows.forEach {
			list.add(it)
		}
		def word = ListUtilities.getOneFromList(list)
		return word
	}
	

}
