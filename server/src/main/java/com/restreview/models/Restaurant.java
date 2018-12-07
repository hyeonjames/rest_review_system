package com.restreview.models;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ForeignKey;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.restreview.dto.DtoMap;
import com.restreview.etc.Mappable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Entity
@Data
public class Restaurant implements Mappable {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long restNo;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="owner_no")
	private RestaurantOwner owner;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="cate_no")
	private Category category;
	
	private String imageUrl;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false)
	private String description;
	@Column(nullable=false)
	private String address;
	private double coordX;
	private double coordY;
	@Column(nullable=false)
	private String phoneNumber;
	private double score;
	private long reviewCount;

	@Column(name="created_at", nullable=false, updatable=false)
	private Date createdAt;

	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	@JoinColumn(name="rest_no")
	private Collection<Review> reviews;
	
	
	@Builder
	protected Restaurant(String name,String description, String address,String phoneNumber,String imageUrl, double coordX, double coordY, RestaurantOwner owner, Category cate) {
		this.name = name;
		this.description = description;
		this.address = address;
		this.coordX = coordX;
		this.coordY = coordY;
		this.imageUrl = imageUrl;
		this.owner = owner;
		this.category = cate;
		this.reviewCount = 0;
		this.score = 0;
		this.phoneNumber = phoneNumber;
		this.createdAt = new Date();
	}


	@Override
	public Object mapping() {
		// TODO Auto-generated method stub
		Restaurant r = this;
		
		return new DtoMap()
				.set("name",name)
				.set("restNo", restNo)
				.set("description",description)
				.set("address", address)
				.set("imageUrl", imageUrl)
				.set("ownerName", owner.name)
				.set("categoryName",category.getName())
				.set("categoryNo", category.getCateNo())
				.set("score", score)
				.set("reviewCount",reviewCount)
				.set("phoneNumber",phoneNumber)
				.set("coordX", coordX)
				.set("coordY" ,coordY);
	}
}
