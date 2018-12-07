package com.restreview.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.restreview.etc.UserType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(UserType.MEMBER)
public class Member extends User{
	
	@Builder
	protected Member(String name, String id, String pw, String email) {
		this.name = name;
		this.userId = id;
		this.setPw(pw);
		this.email = email;
	}

}
