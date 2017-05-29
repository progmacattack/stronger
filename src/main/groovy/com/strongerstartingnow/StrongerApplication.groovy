package com.strongerstartingnow

import com.strongerstartingnow.dao.EnglishWord
import com.strongerstartingnow.dao.EnglishWordDao
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
class StrongerApplication extends WebMvcConfigurerAdapter{

	static void main(String[] args) {
		SpringApplication.run StrongerApplication, args
	}
	
	@Bean
	public CommandLineRunner loadWords(EnglishWordDao dao) {
		dao.buildAllWords();
	}

}
