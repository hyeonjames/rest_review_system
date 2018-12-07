package com.restreview.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.restreview.models.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	@Query("SELECT r FROM Restaurant r WHERE r.category.cateNo = ?1 OR ?1 = 0")
	Page<Restaurant> findAllByCateNo(long cateNo, Pageable p);
	
	@Query("SELECT r FROM Restaurant r WHERE r.name LIKE CONCAT('%',?1,'%') OR r.description LIKE CONCAT('%',?1,'%')"
			+ "	ORDER BY ((r.coordX-?2)*(r.coordX-?2)+(r.coordY-?3)*(r.coordY-?3)) ASC")
	Page<Restaurant> search(String text, double coordX, double coordY, Pageable p);
	
	@Query("SELECT r FROM Restaurant r WHERE r.owner.userNo=?1")
	List<Restaurant> findOwnList(long ownerNo);
	
	
}
