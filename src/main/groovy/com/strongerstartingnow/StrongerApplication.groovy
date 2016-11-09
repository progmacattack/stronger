package com.strongerstartingnow

import com.strongerstartingnow.session.ActiveUserStore
import javax.sql.DataSource
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
class StrongerApplication {

	static void main(String[] args) {
		SpringApplication.run StrongerApplication, args
	}
	
	@Bean
	ActiveUserStore activeUserStore(){
		return new ActiveUserStore()
	}

}
