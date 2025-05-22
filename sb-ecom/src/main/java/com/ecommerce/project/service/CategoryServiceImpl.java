package com.ecommerce.project.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.ecommerce.project.exception.ApiException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortCatgoryBy,
			String sortDIR) {

		// Sorting
		Sort sortByandOrder = sortDIR.equalsIgnoreCase("asc") ? Sort.by(sortCatgoryBy).ascending()
				: Sort.by(sortCatgoryBy).descending();
		
		// Pagination
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize,sortByandOrder);
		Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
		List<Category> cat = categoryPage.getContent();

		if (cat.size() == 0) {
			throw new ApiException("No category exists!!!");
		}

		// DTO with modelMapper
		List<CategoryDTO> categoryDTO = cat.stream().map(category -> modelMapper.map(category, CategoryDTO.class))
				.toList();

		CategoryResponse categoryResponse = new CategoryResponse();

		categoryResponse.setContent(categoryDTO);
		categoryResponse.setPageNumber(categoryPage.getNumber());
		categoryResponse.setPageSize(categoryPage.getSize());
		categoryResponse.setTotalElements(categoryPage.getTotalElements());
		categoryResponse.setTotalPages(categoryPage.getTotalPages());
		categoryResponse.setLastPage(categoryPage.isLast());

		return categoryResponse;
	}

	@Override
	public CategoryDTO createCategory(CategoryDTO categoryDTO) {

		Category map = modelMapper.map(categoryDTO, Category.class);

		Category savedCategory = categoryRepository.findByCategoryName(map.getCategoryName());

		if (savedCategory != null) {
			throw new ApiException("Category with name already exists!!!");
		}

		categoryRepository.save(map);

		return modelMapper.map(map, CategoryDTO.class);
	}

	@Override
	public CategoryDTO deleteMapping(Long id) {

		Optional<Category> categories = categoryRepository.findById(id);

		Category savedCategory = categories
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", id));

		categoryRepository.delete(savedCategory);

		return modelMapper.map(savedCategory, CategoryDTO.class);
	}

	@Override
	public CategoryDTO updateCateory(CategoryDTO categoryDTO, Long id) {

		Category category = modelMapper.map(categoryDTO, Category.class);

		Optional<Category> categories = categoryRepository.findById(id);

		Category existingCategory = categories
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", id));

		existingCategory.setCategoryId(category.getCategoryId());
		existingCategory.setCategoryName(category.getCategoryName());

		Category savedCategory = categoryRepository.save(existingCategory);

		return modelMapper.map(savedCategory, CategoryDTO.class);

	}

}
