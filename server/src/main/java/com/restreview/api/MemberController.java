package com.restreview.api;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restreview.annotations.PreAuth;
import com.restreview.dto.Message;
import com.restreview.etc.SessionManager;
import com.restreview.etc.UserType;
import com.restreview.models.Member;
import com.restreview.repositories.MemberRepository;
import com.restreview.repositories.UserRepository;

@RequestMapping("member")
@RestController
public class MemberController {
	@Autowired SessionManager sessionManager;
	@Autowired MemberRepository repos;
	@PostMapping("/join")
	@Transactional
	@PreAuth(UserType.GUEST)
	public Message join(HttpSession session, 
			@RequestParam(defaultValue="",name="id")String id , 
			@RequestParam(defaultValue="",name="pw")String pw, 
			@RequestParam(defaultValue="",name="email")String email, 
			@RequestParam(defaultValue="",name="name")String name) {
		
		// 정보 유효성 체크
		if(	!id.matches("[a-zA-Z0-9_]{3,12}")
				|| !pw.matches(".{8,16}")
				|| !EmailValidator.getInstance().isValid(email)) {
			return Message.failure("유효하지 않은 정보");
		}
		// 아이디 중복 체크
		if(repos.isDuplicateId(id)) {
			return Message.failure("이미 사용중인 아이디 입니다.");
		}
		// 이메일 중복 체크 
		if(repos.isDuplicateEmail(email)) {
			return Message.failure("이미 사용중인 이메일 입니다.");
		}
		// 멤버 인스턴스 생성 ( 각 함수들은 자동 생성되는 함수들 )
		Member instance = Member.builder()
				.email(email)
				.id(id)
				.name(name)
				.pw(pw)
				.build();
		repos.save(instance);
		return Message.success();
	}
	
	@PostMapping("/update")
	@Transactional
	@PreAuth(UserType.MEMBER)
	public Message update(HttpSession session,
			@RequestParam(defaultValue="",name="pw")String pw, 
			@RequestParam(defaultValue="",name="email")String email, 
			@RequestParam(defaultValue="",name="name")String name) {
		
		if(	!pw.matches(".{8,16}")
				|| !name.matches(".+")
				|| !EmailValidator.getInstance().isValid(email)) { 
			return Message.failure("유효하지 않는 정보");
		}

		
		Member mem = sessionManager.getUser(session);
		// 이메일 중복 체크 
		if(!email.equals(mem.getEmail()) && repos.isDuplicateEmail(email)) {
			return Message.failure("이미 사용중인 이메일 입니다.");
		}
		mem.setPw(pw);
		mem.setEmail(email);
		mem.setName(name);
		repos.save(mem);
		return Message.success();
	}
	
	@PostMapping("/delete")
	@Transactional
	@PreAuth(UserType.MEMBER)
	public Message delete(HttpSession session, 
			@RequestParam(defaultValue="",name="id")String id , 
			@RequestParam(defaultValue="",name="pw")String pw) {
		Member mem = sessionManager.getUser(session);
		// 아이디 패스워드 일치 여부 확인
		if(!mem.getUserId().equals(id)) {
			return Message.failure("아이디가 일치하지 않습니다.");
		}
		if(!mem.isEqualPw(pw)) {
			return Message.failure("패스워드가 일치하지 않습니다.");
		}
		// 데이터베이스에서 정보 삭제
		repos.delete(mem);
		// 세션 정보 삭제
		sessionManager.expire(session);
		return Message.success();
	}
}
