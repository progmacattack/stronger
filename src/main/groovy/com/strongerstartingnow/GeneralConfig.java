package com.strongerstartingnow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import com.strongerstartingnow.thymeleaf.dialect.UtilDialect;

@Configuration
public class GeneralConfig {

	@Bean
	public UtilDialect utilDialect() {
		return new UtilDialect();
	}
}
