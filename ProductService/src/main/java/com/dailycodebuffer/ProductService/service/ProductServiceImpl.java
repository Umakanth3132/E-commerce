package com.dailycodebuffer.ProductService.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dailycodebuffer.ProductService.Exception.ProductServiceCustomException;
import com.dailycodebuffer.ProductService.entity.Product;
import com.dailycodebuffer.ProductService.model.ProductRequest;
import com.dailycodebuffer.ProductService.model.ProductResponse;
import com.dailycodebuffer.ProductService.repository.ProduceRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProduceRepository productRepository;

	@Override
	public long addProduct(ProductRequest productRequest) {

		log.info("Adding Product..");

		Product product = Product.builder().productName(productRequest.getName()).quantity(productRequest.getQuantity())
				.price(productRequest.getPrice()).build();
		productRepository.save(product);

		log.info("Product Created");

		return product.getProductId();
	}

	@Override
	public ProductResponse getProductById(long productId) {
		log.info("Get the product for productId: {}", productId);

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ProductServiceCustomException("Product with give Id not found","PRODUCT_NOT_FOUND"));

		ProductResponse produceResponse = new ProductResponse();

		BeanUtils.copyProperties(product, produceResponse);

		return produceResponse;
	}

	@Override
	public void reduceQuantity(long productId, long quantity) {
		// TODO Auto-generated method stub
		log.info("Reduce Quantity {} for Id: {}",quantity, productId);
		
		Product product=productRepository.findById(productId)
				.orElseThrow(()-> new ProductServiceCustomException("Product with given Id not found", "PRODUCT_NOT_FOUND"));
		if(product.getQuantity()<quantity) {
			throw new ProductServiceCustomException("Product does not have sufficient Quantity", "INSUFFICIENT_QUANTITY");
		}
		product.setQuantity(product.getQuantity()-quantity);
		productRepository.save(product);
		
		log.info("Product Quantity updated Successfully");
	}

}
