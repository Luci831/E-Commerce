package com.ecommerce.project.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;

public interface ProductService {

	 ProductDto addProduct(Long categoryId,ProductDto productDto);

	 ProductResponse getProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

	 ProductResponse getProductsByCategory(Long categoryId);

	 ProductResponse getProductsByKeyword(String keyword);

	 ProductDto updateProduct(ProductDto productDto, Long productId);

	 ProductDto deleteProduct(Long id);

	 ProductDto updateImage(Long productId, MultipartFile image) throws IOException;
}
