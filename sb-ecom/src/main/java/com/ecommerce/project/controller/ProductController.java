package com.ecommerce.project.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {
	
	@Autowired
	ProductService productService;

	@PostMapping("/admin/categories/{categoryId}/product")
	public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto,
			                                      @PathVariable Long categoryId)
	{
		ProductDto savedProduct = productService.addProduct(categoryId,productDto);
		
		return new ResponseEntity<ProductDto>(savedProduct,HttpStatus.OK);
	}
	@GetMapping("/public/products")
	public ResponseEntity<ProductResponse> getAllProducts()
	{
		ProductResponse products = productService.getProducts();
		
		return new ResponseEntity<ProductResponse>(products,HttpStatus.OK);
	}
	@GetMapping("/public/categories/{categoryId}/products")
	public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable Long categoryId )
	{
		
		ProductResponse products=productService.getProductsByCategory(categoryId);
		
		return new ResponseEntity<>(products,HttpStatus.OK);
	}
	@GetMapping("/public/products/keyword/{keyword}")
	public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword)
	{
		ProductResponse products=productService.getProductsByKeyword(keyword);
		
		return new ResponseEntity<ProductResponse>(products,HttpStatus.FOUND);
	}
	
	@PutMapping("/admin/products/{productId}")
	public ResponseEntity<ProductDto> updateProducts(@RequestBody ProductDto productDto,
			                                         @PathVariable Long productId)
	{
		ProductDto updatedProduct=productService.updateProduct(productDto,productId);
		
		return new ResponseEntity<ProductDto>(updatedProduct,HttpStatus.OK);
	}
	@DeleteMapping("/admin/products/{productId}")
	public ResponseEntity<ProductDto> deleteProduct(@PathVariable(name="productId") Long id)
	{
		
		ProductDto deletedProduct=productService.deleteProduct(id);
		
		return new ResponseEntity<ProductDto>(deletedProduct,HttpStatus.OK);
		
		
	}
	@PutMapping("/admin/products/{productId}/image")
	public ResponseEntity<ProductDto> updateImage(@PathVariable Long productId, @RequestParam("image")MultipartFile image) throws IOException
	{
		ProductDto updatedProductImage=productService.updateImage(productId,image);
		
		return new ResponseEntity<ProductDto>(updatedProductImage,HttpStatus.OK);
		
	}
	
	
}
