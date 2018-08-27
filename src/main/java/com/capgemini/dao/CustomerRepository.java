package com.capgemini.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.domain.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long>, 
CustomerRepositoryCustom {

	
	
	
}
