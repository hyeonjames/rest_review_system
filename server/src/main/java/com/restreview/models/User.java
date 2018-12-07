package com.restreview.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restreview.dto.DtoMap;
import com.restreview.etc.Mappable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="utype", discriminatorType=DiscriminatorType.STRING, length=10)
public abstract class User implements Mappable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonIgnore
	@Getter
	protected Long userNo;
	
	@Getter
	@Column(length=40, nullable=false,unique=true)
	protected String userId;
	
	@Column(length=256,nullable=false)
	@JsonIgnore
	protected String userPw;
	
	@Column(length=100, nullable=false)
	@Getter
	@Setter
	protected String email;
	
	@Column(length=20, nullable=false)
	@Getter
	@Setter
	protected String name;
	
	@Column(length=10, nullable=false,insertable=false, updatable=false)
	@Getter
	protected String utype;

	@Column(name="created_at", nullable=false, updatable=false)
	@JsonIgnore
	@Getter
	private Date createdAt;
	
	
	protected User() {
		createdAt = new Date();
		
	}
	public void setPw(String pw) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String encodedPw = encoder.encode(pw);
		this.userPw = encodedPw;
		
	}
	public boolean isEqualPw(String pw) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(pw, this.userPw);
	}
	public Object mapping() {
		return new DtoMap()
				.set("userId", userId)
				.set("userNo",userNo)
				.set("name", name)
				.set("email",email)
				.set("type", utype);
	}
}
