package com.restreview.models;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.restreview.etc.UserType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue(UserType.RESTAURANTOWNER)
public class RestaurantOwner extends User{
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="owner_no")
	Collection<Restaurant> restaurants;

	@Builder
	protected RestaurantOwner(String name, String id, String pw, String email) {
		this.name = name;
		this.userId = id;
		this.setPw(pw);
		this.email = email;
	}
}
