package com.restreview.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.restreview.models.Member;

public interface MemberRepository extends UserRepository<Member> {
	
	@Query("SELECT d FROM Member d")
	public Page<Member> manageList(Pageable pg);
}
