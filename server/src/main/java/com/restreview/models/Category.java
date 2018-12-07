package com.restreview.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class Category {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long cateNo;
	
	private String name;
	
	
}
