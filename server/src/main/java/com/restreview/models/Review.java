package com.restreview.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import javax.persistence.OneToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.restreview.dto.DtoMap;
import com.restreview.etc.Mappable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class Review implements Mappable {
	
	
	@Builder
	private Review(String content, String imageUrl, Restaurant restaurant, int score, User writer ) {
		this.content = content;
		this.imageUrl = imageUrl;
		this.restaurant = restaurant;
		this.score = score;
		this.writer = writer;
		this.writerName = writer.getName();
		this.createdAt = new Date();
	}
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long reviewNo;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="rest_no")
	private Restaurant restaurant;
	
	@OneToOne(mappedBy="review", fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="review_no")
	private ReviewReport report;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="user_no")
	private User writer;
	
	@Column(length=40)
	private String writerName;
	
	private String imageUrl;
	private String content;
	private int score;
	
	@Column(name="created_at", nullable=false, updatable=false)
	private Date createdAt;


	@Override
	public Object mapping() {
		// TODO Auto-generated method stub
		Restaurant r = restaurant;
		DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return new DtoMap()
			.set("reviewNo", reviewNo)
			.set("imageUrl",imageUrl)
			.set("content",content)
			.set("writerName", writerName)
			.set("score",score)
			.set("createdAt",dformat.format(createdAt))
			.set("restaurantName", r.getName())
			.set("reportState", report == null ? "NONE" : report.getState());
		
	}
	
}
