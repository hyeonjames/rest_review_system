package com.restreview.api;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restreview.annotations.PreAuth;
import com.restreview.dto.DtoMap;
import com.restreview.dto.Message;
import com.restreview.etc.PasswordGenerator;
import com.restreview.etc.UserType;
import com.restreview.models.User;
import com.restreview.repositories.UserRepository;

@RequestMapping("user")
@RestController
public class UserController {
	
	@Autowired UserRepository<User> repos;
	@Autowired PasswordGenerator generator;
	
	@PostMapping("/find-id")
	@PreAuth(UserType.GUEST)
	
	public Message findId(HttpSession session, 
			@RequestParam(defaultValue="",name="email")String email, 
			@RequestParam(defaultValue="",name="name")String name) {
		String id = repos.findUserId(name,email);
		if(id == null) {
			return Message.failure("해당 정보와 일치하는 사용자를 찾을 수 없습니다.");
		}
		
		return Message.success(new DtoMap().set("id", id));
	}
	
	@PostMapping("/find-pw")
	@Transactional
	@PreAuth(UserType.GUEST)
	
	public Message findPw(HttpSession session, 
			@RequestParam(defaultValue="",name="id")String id ,
			@RequestParam(defaultValue="",name="email")String email, 
			@RequestParam(defaultValue="",name="name")String name) {
		User u = repos.findWithoutPw(id,name, email);
		
		if(u == null) {
			return new Message(false, "해당 정보와 일치하는 사용자를 찾을 수 없습니다.");
		}
		String generatedPw = generator.generate();
		u.setPw(generatedPw);
		repos.save(u);
		return Message.success(new DtoMap().set("pw", generatedPw));
	}
	
	@GetMapping("/check-id/{id}")
	@PreAuth(UserType.GUEST)
	public Message checkId(HttpSession session, @PathVariable("id") String id) {
		return Message.success(new DtoMap().set("value", !repos.isDuplicateId(id)));
	}
}
