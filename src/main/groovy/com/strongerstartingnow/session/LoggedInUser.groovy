package com.strongerstartingnow.session
import javax.servlet.http.HttpSessionBindingEvent
import javax.servlet.http.HttpSessionBindingListener
import org.springframework.stereotype.Component

@Component
class LoggedInUser implements HttpSessionBindingListener{
	String username
	ActiveUserStore activeUserStore
	
	@Override
	void valueBound(HttpSessionBindingEvent event) {
		List<String> users = activeUserStore.users
		LoggedInUser loggedInUser = (LoggedInUser)event.getValue()
		if (!users.contains(loggedInUser.username)) {
			users.add(loggedInUser.activeUserStore)
		}
	}
	
	@Override
	void valueUnbound(HttpSessionBindingEvent event) {
		List<String> users = activeUserStore.users
		LoggedInUser loggedInUser = (LoggedInUser)event.getValue()
		if (users.contains(loggedInUser.username)) {
			users.remove(loggedInUser.activeUserStore)
		}
	}
	
}
