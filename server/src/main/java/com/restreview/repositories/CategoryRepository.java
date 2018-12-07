package com.restreview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restreview.models.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {

}
