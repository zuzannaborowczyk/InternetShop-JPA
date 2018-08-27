package com.capgemini.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.capgemini.dao.CustomerRepositoryCustom;
import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.QCustomerEntity;
import com.capgemini.domain.QProductEntity;
import com.capgemini.domain.QTransactionEntity;
import com.capgemini.domain.QTransactionProductEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;
	
	

	@Override
	public List<CustomerEntity> findThreeCustomersWhoSpendTheMostInPeriod(Date beginDate, Date endDate) {
		QTransactionEntity transactionEntity = QTransactionEntity.transactionEntity;
		QProductEntity productEntity = QProductEntity.productEntity;
		QCustomerEntity customerEntity = QCustomerEntity.customerEntity;
		QTransactionProductEntity transactionProductEntity = QTransactionProductEntity.transactionProductEntity;

		int amountOfCustomers = 3;
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		List<CustomerEntity> threeClients = query.from(customerEntity)
				.join(customerEntity.transactions, transactionEntity)
				.join(transactionEntity.products, transactionProductEntity)
				.join(transactionProductEntity.product, productEntity).select(customerEntity)
				.groupBy(customerEntity).where(transactionEntity.date.between(beginDate, endDate))
				.orderBy((productEntity.price.multiply(transactionProductEntity.numberOfBoughtItems)).sum().desc())
				.limit(amountOfCustomers).fetch();
		return threeClients;
	}

}
