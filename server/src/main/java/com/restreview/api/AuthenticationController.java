package com.restreview.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restreview.annotations.PreAuth;
import com.restreview.dto.Message;
import com.restreview.etc.SessionManager;
import com.restreview.etc.UserType;
import com.restreview.models.User;
import com.restreview.repositories.UserRepository;

@RequestMapping("auth")
@RestController
public class AuthenticationController {
	
	@Autowired
	UserRepository<User> userRepos;
	
	@Autowired
	SessionManager manager;
	
	
	@PreAuth(UserType.GUEST)
	@PostMapping("/login")
	public Object login(HttpSession session, 
			@RequestParam(defaultValue="",name="id")String id , 
			@RequestParam(defaultValue="",name="pw")String pw) {
		
		User us = userRepos.findByUserId(id);
		if(us == null) {
			return Message.failure("해당 정보와 일치하는 회원을 찾을 수 없습니다.");
		}
		if(!us.isEqualPw(pw)) {
			return Message.failure("비밀번호가 일치하지 않습니다.");
		}
		manager.setUser(session, us);
		
		return Message.dto(us);
	}
	
	@PreAuth(UserType.USER)
	@GetMapping("/logout")
	public Message logout(HttpSession session) {
		manager.expire(session);
		return Message.success();
	}
	
	@PreAuth(UserType.USER)
	@GetMapping("/info")
	public Message info(HttpSession session) {
		User us = manager.getUser(session);
		return Message.dto(us);
	}
}
