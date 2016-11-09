package com.strongerstartingnow.session
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component("myAuthenticationSuccessHandler")
class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
 
	@Autowired
    ActiveUserStore activeUserStore;
     
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
      HttpServletResponse response, Authentication authentication) 
      throws IOException {
        HttpSession session = request.getSession(false);
        println "Session is: " + session.toString();
		println "Logged in user is: " + authentication.getName();
		if (session != null) {
			
            LoggedInUser user = new LoggedInUser(username:authentication.getName(), activeUserStore: activeUserStore)
            session.setAttribute("user", user);
        }
    }
}
