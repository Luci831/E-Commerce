package com.ecommerce.project.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.project.exception.ApiException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	FileServiceImpl fileServiceImpl;

	@Value("${project.image}")
	private String path;

	@Override
	public ProductDto addProduct(Long categoryId, ProductDto productDto) {
		// TODO Auto-generated method stub

		// validation for product already present

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));

		boolean isProductNotPresent = true;

		List<Product> products = category.getProduct();

		for (Product val : products) {
			if (val.getProductName().equals(productDto.getProductName()))
				isProductNotPresent = false;
			break;
		}

		if (isProductNotPresent) {
			Product product = modelMapper.map(productDto, Product.class);

			product.setCategory(category);

			double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());

			product.setSpecialPrice(specialPrice);

			product.setImage("default.png");

			Product savedProduct = productRepository.save(product);

			return modelMapper.map(savedProduct, ProductDto.class);
		} else {
			throw new ApiException("Product Already present!!");
		}

	}

	@Override
	public ProductResponse getProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		// TODO Auto-generated method stub

		// Pagination
		Sort sortByandOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByandOrder);
		Page<Product> pageProducts = productRepository.findAll(pageDetails);

		List<Product> products = pageProducts.getContent();

		// Setting pageResponse
		List<Product> findAll = productRepository.findAll();
		List<ProductDto> list = findAll.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();

		ProductResponse productResponse = new ProductResponse();

		productResponse.setContent(list);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		productResponse.setLastPage(pageProducts.isLast());

		// Validation for if product in DB =0

		if (findAll.isEmpty())
			throw new ApiException("No products found in DB!!");

		return productResponse;
	}

	@Override
	public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortOrder) {
		// TODO Auto-generated method stub

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));

		// Pagination
		Sort sortByandOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByandOrder);
		Page<Product> pageProducts = productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);

		List<Product> products = pageProducts.getContent();

		List<ProductDto> map = products.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();

		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(map);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		productResponse.setLastPage(pageProducts.isLast());

		return productResponse;
	}

	@Override
	public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
			String sortOrder) {
		// TODO Auto-generated method stub
		
		//Pagination
		Sort sortByandOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByandOrder);
		Page<Product> pageProducts = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%',pageDetails);

		List<Product> products = pageProducts.getContent();
		
		if(products.isEmpty())
			throw new ApiException("Product not found with keyword "+ keyword);
		
		List<ProductDto> map = products.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();

		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(map);
		productResponse.setContent(map);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		productResponse.setLastPage(pageProducts.isLast());

		return productResponse;
	}

	@Override
	public ProductDto updateProduct(ProductDto productDto, Long productId) {
		// TODO Auto-generated method stub

		Product productById = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("product", "productId", productId));

		Product product = modelMapper.map(productDto, Product.class);

		productById.setCategory(product.getCategory());
		productById.setDiscount(product.getDiscount());
		productById.setImage(product.getImage());
		productById.setPrice(product.getPrice());
		productById.setProductDescription(product.getProductDescription());
		productById.setProductName(product.getProductName());
		productById.setQuantity(product.getQuantity());
		productById.setSpecialPrice(product.getSpecialPrice());

		Product savedProduct = productRepository.save(productById);

		ProductDto updatedProduct = modelMapper.map(savedProduct, ProductDto.class);

		return updatedProduct;
	}

	@Override
	public ProductDto deleteProduct(Long id) {
		// TODO Auto-generated method stub

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("product", "productId", id));
		ProductDto productDTO = modelMapper.map(product, ProductDto.class);

		productRepository.delete(product);

		return productDTO;
	}

	@Override
	public ProductDto updateImage(Long productId, MultipartFile image) throws IOException {
		// TODO Auto-generated method stub

		// Get the product from DB

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("product", "productId", productId));

		// Upload image to server
		// Get the file name of uploaded image

		String fileName = fileServiceImpl.uploadImage(path, image);

		// Updating the new file name to the product
		product.setImage(fileName);

		// Save product
		Product updatedProduct = productRepository.save(product);

		// return DTO after mapping Product to DTO
		ProductDto productDto = modelMapper.map(updatedProduct, ProductDto.class);

		return productDto;
	}

}
