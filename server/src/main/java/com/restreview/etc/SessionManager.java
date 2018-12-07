package com.restreview.etc;

import javax.servlet.http.HttpSession;

import com.restreview.models.User;

public class SessionManager {

	public SessionManager() {
		
	}
	
	@SuppressWarnings("unchecked")
	public <T extends User> T getUser(HttpSession session) {
		
		return (T) session.getAttribute("userSession");
	}
	
	public void setUser(HttpSession session, User u) {
		session.setAttribute("userSession", u);
	}
	
	public void expire(HttpSession session) {
		session.removeAttribute("userSession");
	}
	
}
