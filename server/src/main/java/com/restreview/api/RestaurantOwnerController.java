package com.restreview.api;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restreview.annotations.PreAuth;
import com.restreview.dto.Message;
import com.restreview.etc.SessionManager;
import com.restreview.etc.UserType;
import com.restreview.models.RestaurantOwner;
import com.restreview.repositories.RestaurantOwnerRepository;

@RequestMapping("owner")
@RestController
public class RestaurantOwnerController {

	@Autowired RestaurantOwnerRepository repos;
	@Autowired SessionManager sessionManager;
	
	@PostMapping("/join")
	@Transactional
	@PreAuth(UserType.GUEST)
	public Message join(HttpSession session,
			@RequestParam(defaultValue="",name="id")String id , 
			@RequestParam(defaultValue="",name="pw")String pw, 
			@RequestParam(defaultValue="",name="email")String email, 
			@RequestParam(defaultValue="",name="name")String name) {

		// 데이터 유효성 체크
		if(!id.matches("[a-zA-Z0-9_]{3,12}")
				|| !pw.matches(".{8,16}")
				|| !EmailValidator.getInstance().isValid(email)
				|| name.isEmpty()) {
			return Message.failure("유효하지 않은 정보");
		}
		if(repos.isDuplicateId(id)) {
			return Message.failure("이미 사용중인 아이디입니다.");
		}
		if(repos.isDuplicateEmail(email)) {
			return Message.failure("이미 사용중인 이메일입니다.");
		}
		RestaurantOwner instance = RestaurantOwner.builder().email(email).id(id).name(name).pw(pw).build();
		
		repos.save(instance);
		return Message.success();
	}

	@PostMapping("/update")
	@Transactional
	@PreAuth(UserType.RESTAURANTOWNER)
	public Message update(HttpSession session,
			@RequestParam(defaultValue="",name="pw")String pw, 
			@RequestParam(defaultValue="",name="email")String email, 
			@RequestParam(defaultValue="",name="name")String name) {

		// 데이터 유효성 체크
		if(!pw.matches(".{8,16}")
				|| !EmailValidator.getInstance().isValid(email)
				|| name.isEmpty()) {
			return Message.failure("유효하지 않은 정보");
		}

		
		RestaurantOwner owner = sessionManager.getUser(session);
		// 이메일 중복 체크 
		if(!email.equals(owner.getEmail()) && repos.isDuplicateEmail(email)) {
			return Message.failure("이미 사용중인 이메일 입니다.");
		}
		owner.setPw(pw);
		owner.setEmail(email);
		owner.setName(name);
		repos.save(owner);
		return Message.success();
	}

	@PostMapping("/delete")
	@Transactional
	@PreAuth(UserType.RESTAURANTOWNER)
	public Message delete(HttpSession session, 
			@RequestParam(defaultValue="",name="id")String id , 
			@RequestParam(defaultValue="",name="pw")String pw) {
		RestaurantOwner owner = sessionManager.getUser(session);
		if(!owner.getUserId().equals(id)) {
			return Message.failure("아이디가 올바르지 않습니다.");
		}
		if(!owner.isEqualPw(pw)) {
			return Message.failure("비밀번호가 올바르지 않습니다.");
		}
		repos.delete(owner);
		sessionManager.expire(session);
		return Message.success();
	}
}
