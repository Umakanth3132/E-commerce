package com.dailycodebuffer.ProductService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailycodebuffer.ProductService.entity.Product;

@Repository
public interface ProduceRepository extends JpaRepository<Product, Long>{

}
