package com.restreview.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.restreview.models.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
	@Query("SELECT AVG(r.score) FROM Review r WHERE r.restaurant.restNo = ?1")
	public Double getAvgScore(Long no);
	
	@Query("SELECT r FROM Review r WHERE r.restaurant.restNo = ?1")
	public Page<Review> getList(long restno, Pageable pg);
	
	@Query("SELECT COUNT(r.reviewNo) FROM Review r WHERE r.restaurant.restNo = ?1")
	public int getReviewCountByRestNo(long restNo);

	@Query("SELECT COUNT(r.reviewNo) FROM Review r WHERE r.writer.userNo=?1")
	public int getReviewCountByUserNo(long userNo);
	@Query("SELECT COUNT(r.reviewNo) FROM Review r WHERE r.writer.userNo=?1 AND r.report IS NOT NULL AND r.report.state=?2")
	public int getReportedReviewCount(long userNo, String state);
}
