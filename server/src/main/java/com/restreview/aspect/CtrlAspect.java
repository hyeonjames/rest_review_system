package com.restreview.aspect;

import java.lang.reflect.Method;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.restreview.annotations.PreAuth;
import com.restreview.dto.Message;
import com.restreview.etc.SessionManager;
import com.restreview.etc.UserType;
import com.restreview.models.User;

@Aspect
@Component
public class CtrlAspect {
	@Autowired
	private SessionManager manager;
	
	@Around("@annotation(com.restreview.annotations.PreAuth)")
	public Object around(ProceedingJoinPoint jp) throws Throwable {
		// 메서드 정보 가져옴
		Method method = ((MethodSignature)jp.getSignature()).getMethod();
		// PreAuth 어노테이션 정보 가져옴
		PreAuth auth = method.getAnnotation(PreAuth.class);
		// 해당 메서드가 필요로 하는 권한 정보를 가져옴
		String[] arr = auth.value();
		// ALL인 경우 그냥 실행하면 됨
		if(arr[0].equals(UserType.ALL)) {
			return jp.proceed();
		}
		
		// 메서드에서 Session 정보를 가져옴
		HttpSession session = null;
		for(Object obj : jp.getArgs()) {
			if(obj instanceof HttpSession) {
				session = (HttpSession)obj;
			}
		}
		if(session == null) {
			throw new Exception("세션 정보를 찾을 수 없습니다.");
		}
		
		// 세션에서 현재 접속한 유저정보 가져옴
		User us = manager.getUser(session);
		
		// 현재 접속한 유저가 해당 메서드의 권한을 가지고 있는지 체크
		String type = UserType.GUEST;
		if(us != null) type = us.getUtype();
		for(String tp : arr) {
			if(tp.equals(type) || (tp.equals(UserType.USER) && !type.equals(UserType.GUEST))) {
				return jp.proceed();
			}
		}
		return Message.failure("권한이 없습니다.");
	}
}
