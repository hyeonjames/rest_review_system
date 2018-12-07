package com.restreview.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.restreview.models.ReviewReport;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, Long>{
	
	public Page<ReviewReport> findAll(Pageable pg);
}
