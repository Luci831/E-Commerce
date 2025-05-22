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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));
		
		Product product=modelMapper.map(productDto, Product.class);

		product.setCategory(category);

		double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());

		product.setSpecialPrice(specialPrice);

		product.setImage("default.png");

		Product savedProduct = productRepository.save(product);

		return modelMapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public ProductResponse getProducts() {
		// TODO Auto-generated method stub

		List<Product> findAll = productRepository.findAll();

		List<ProductDto> list = findAll.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();

		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(list);

		return productResponse;
	}

	@Override
	public ProductResponse getProductsByCategory(Long categoryId) {
		// TODO Auto-generated method stub

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));

		List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);

		List<ProductDto> map = products.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();

		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(map);

		return productResponse;
	}

	@Override
	public ProductResponse getProductsByKeyword(String keyword) {
		// TODO Auto-generated method stub

		List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%');

		List<ProductDto> map = products.stream().map(product -> modelMapper.map(product, ProductDto.class)).toList();

		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(map);

		return productResponse;
	}

	@Override
	public ProductDto updateProduct(ProductDto productDto, Long productId) {
		// TODO Auto-generated method stub
		
		Product productById= productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("product", "productId", productId));
		
		Product product=modelMapper.map(productDto, Product.class);
		
		productById.setCategory(product.getCategory());
		productById.setDiscount(product.getDiscount());
		productById.setImage(product.getImage());
		productById.setPrice(product.getPrice());
		productById.setProductDescription(product.getProductDescription());
		productById.setProductName(product.getProductName());
		productById.setQuantity(product.getQuantity());
		productById.setSpecialPrice(product.getSpecialPrice());
		
		Product savedProduct = productRepository.save(productById);
		
		ProductDto updatedProduct=modelMapper.map(savedProduct, ProductDto.class);
		
		return updatedProduct;
	}

	@Override
	public ProductDto deleteProduct(Long id) {
		// TODO Auto-generated method stub
		
		Product product=productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("product", "productId", id));
		ProductDto productDTO=modelMapper.map(product, ProductDto.class);
		
		productRepository.delete(product);
		
		return productDTO;
	}

	@Override
	public ProductDto updateImage(Long productId, MultipartFile image) throws IOException {
		// TODO Auto-generated method stub
		
		//Get the product from DB
		
		Product product=productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("product", "productId", productId));
		
		//Upload image to server
		//Get the file name of uploaded image
		
		String fileName= fileServiceImpl.uploadImage(path, image);
		
		//Updating the new file name to the product
		product.setImage(fileName);
		
		//Save product
		Product updatedProduct=productRepository.save(product);
		
		//return DTO after mapping Product to DTO
		ProductDto productDto=modelMapper.map(updatedProduct, ProductDto.class);
		
		
		
		return productDto;
	}

	

}
