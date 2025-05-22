package com.ecommerce.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByCategoryOrderByPriceAsc(Category category);

	List<Product> findByProductNameLikeIgnoreCase(String keyword);
	

}
