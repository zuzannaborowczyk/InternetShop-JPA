package com.capgemini.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.domain.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, 
ProductRepositoryCustom {

}
