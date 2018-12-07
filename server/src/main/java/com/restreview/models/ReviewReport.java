package com.restreview.models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.restreview.dto.DtoMap;
import com.restreview.etc.Mappable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class ReviewReport implements Mappable {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long reportNo;
	
	@OneToOne(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
	@JoinColumn(name="review_no")
	private Review review;
	
	private String state;
	
	private String reason;

	@Column(name="created_at", nullable=false, updatable=false)
	private Date createdAt;
	
	@Builder
	protected ReviewReport(String state,String reason, Review review) {
		this.state = state;
		this.review = review;
		this.reason = reason;
		this.createdAt = new Date();
	}

	@Override
	public Object mapping() {
		// TODO Auto-generated method stub
		Restaurant r = review.getRestaurant();
		
		return new DtoMap()
			.set("reportNo", reportNo)
			.set("reviewNo",review.getReviewNo())
			.set("restaurant", new DtoMap()
					.set("name", r.getName())
					.set("score", r.getScore())
					.set("reviewCount", r.getReviewCount())
					.set("imageUrl", r.getImageUrl()))
			.set("writer", review.getWriterName())
			.set("content", review.getContent())
			.set("state", state)
			.set("reason", reason);
	}
}
