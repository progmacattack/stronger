package com.strongerstartingnow

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@SpringBootApplication
class StrongerApplication extends WebMvcConfigurerAdapter{

	static void main(String[] args) {
		SpringApplication.run StrongerApplication, args
	}

}
