package com.restreview.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.restreview.models.User;

public interface UserRepository<T extends User> extends JpaRepository<T, Long>{
	
	@Query("SELECT u.utype FROM #{#entityName} u WHERE userId=?1 and userPw=?2")
	public String getType(String id, String pw);
	
	@Query("SELECT (CASE WHEN COUNT(u.userId) > 0 THEN TRUE ELSE FALSE END) FROM User u WHERE u.userId=?1")
	public boolean isDuplicateId(String id);
	
	@Query("SELECT (CASE WHEN COUNT(u.email) > 0 THEN TRUE ELSE FALSE END) FROM User u WHERE u.email=?1")
	public boolean isDuplicateEmail(String email);
	
	public T findByUserId(String id);
	
	@Query("SELECT u.userId FROM User u WHERE u.name = ?1 and u.email = ?2")
	public String findUserId(String name,String email);
	
	@Query("SELECT u FROM User u WHERE u.userId=?1 AND u.name=?2 AND u.email=?3")
	public User findWithoutPw(String id, String name, String email);
	
}
