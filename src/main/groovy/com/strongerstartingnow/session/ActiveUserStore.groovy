package com.strongerstartingnow.session
import org.springframework.stereotype.Component

@Component
class ActiveUserStore {
	List<String> users;
}
