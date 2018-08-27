package com.capgemini.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.domain.TransactionProductEntity;

public interface TransactionProductRepository extends JpaRepository<TransactionProductEntity, Long>, 
TransactionProductRepositoryCustom {

}
