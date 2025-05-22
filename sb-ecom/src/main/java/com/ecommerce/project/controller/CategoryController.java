package com.ecommerce.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ecommerce.project.config.AppConstant;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;

import jakarta.validation.Valid;

@RestController
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/api/public/categories")
	public ResponseEntity<CategoryResponse> getAllCategories(
			@RequestParam(name="pageNumber" ,defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name="pageSize" ,defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
	        @RequestParam(name="sortBy" ,defaultValue = AppConstant.SORT_CATEGORY_BY, required = false) String sortBy,
            @RequestParam(name="sortOrder" ,defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder){
		CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber, pageSize,sortBy,sortOrder);

		return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
	}

	@PostMapping("/api/admin/category")
	public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {

		categoryService.createCategory(categoryDTO);

		return new ResponseEntity<>(categoryDTO, HttpStatus.CREATED);
	}

	@DeleteMapping("api/admin/category/{id}")
	public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id) {

		CategoryDTO dto = categoryService.deleteMapping(id);
		return new ResponseEntity<>(dto, HttpStatus.OK);

	}

	@PutMapping("/api/admin/categories/{id}")
	public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
			@PathVariable Long id) {

		CategoryDTO dto = categoryService.updateCateory(categoryDTO, id);

		return new ResponseEntity<>(dto, HttpStatus.OK);

	}

}
