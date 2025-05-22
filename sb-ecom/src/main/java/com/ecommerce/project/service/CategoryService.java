package com.ecommerce.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;


public interface CategoryService {
	
	CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortCategoryBy,String sortDIR);
	
	CategoryDTO createCategory(CategoryDTO categoryDTO);
	
	CategoryDTO deleteMapping(Long id);
	
	CategoryDTO updateCateory(CategoryDTO categoryDTO, Long id);

}
