package com.dailycodebuffer.OrderService.external.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dailycodebuffer.OrderService.Exception.CustomException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
@CircuitBreaker(name="external", fallbackMethod="fallback")
@FeignClient(name = "PRODUCT-SERVICE/product")
public interface ProductService {

	@PutMapping("/reduceQuantity/{id}")
	ResponseEntity<Void> reduceQuantity(@PathVariable("id") long productId, @RequestParam long quantity);

	default void fallback(Exception e) {

		throw new CustomException("Product Service is not available", "Unavailable", 500);
	}

}
