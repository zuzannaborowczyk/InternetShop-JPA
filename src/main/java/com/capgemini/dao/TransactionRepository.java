package com.capgemini.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.domain.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>, 
TransactionRepositoryCustom {

}
