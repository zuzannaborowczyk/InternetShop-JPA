package com.capgemini.dao.impl;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.capgemini.dao.TransactionRepositoryCustom;
import com.capgemini.domain.QCustomerEntity;
import com.capgemini.domain.QProductEntity;
import com.capgemini.domain.QTransactionEntity;
import com.capgemini.domain.QTransactionProductEntity;
import com.capgemini.domain.Status;
import com.capgemini.domain.TransactionEntity;
import com.capgemini.types.SearchCriteria;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class TransactionRepositoryImpl implements TransactionRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<TransactionEntity> findTransactionsBySearchCriteria(SearchCriteria searchCriteria) {

		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

		BooleanBuilder builder = new BooleanBuilder();

		QTransactionEntity transactionEntity = QTransactionEntity.transactionEntity;

		QCustomerEntity customerEntity = QCustomerEntity.customerEntity;

		QTransactionProductEntity transactionProductEntity = QTransactionProductEntity.transactionProductEntity;

		QProductEntity productEntity = QProductEntity.productEntity;

		if (searchCriteria.getCustomerName() != null) {
			builder.and(transactionEntity.customer.lastName.eq(searchCriteria.getCustomerName()));
		}
		if (searchCriteria.getTransactionCost() != null) {
			builder.and(transactionProductEntity.numberOfBoughtItems.multiply(transactionProductEntity.product.price)
					.sum().doubleValue().eq(searchCriteria.getTransactionCost()));
		}
		if (searchCriteria.getBeginDate() != null && searchCriteria.getEndDate() != null) {
			builder.and(transactionEntity.date.between(searchCriteria.getBeginDate(), searchCriteria.getEndDate()));
		}
		if (searchCriteria.getProductName() != null) {
			builder.and(productEntity.productName.eq(searchCriteria.getProductName()));
		}

		List<TransactionEntity> foundTransactions = queryFactory.select(transactionEntity).from(transactionEntity)
				.join(transactionEntity.customer, customerEntity)
				.join(transactionEntity.products, transactionProductEntity)
				.join(transactionProductEntity.product, productEntity)

				.where(builder).fetch();
		return foundTransactions;

	}

	@Override
	public double calculateTotalCostOfTransactionsWithStatus(Status status) {
		JPAQuery<Double> query = new JPAQuery(entityManager);
		QTransactionEntity transactionEntity = QTransactionEntity.transactionEntity;
		QTransactionProductEntity transactionProductEntity = QTransactionProductEntity.transactionProductEntity;
		QProductEntity productEntity = QProductEntity.productEntity;

		Double totalCostOftransactions = query.from(transactionEntity)
				.select((transactionProductEntity.numberOfBoughtItems.multiply(productEntity.price).doubleValue()).sum()
						.doubleValue())
				.join(transactionEntity.products, transactionProductEntity)
				.join(transactionProductEntity.product, productEntity).where(transactionEntity.status.eq(status))
				.fetchOne();
		return totalCostOftransactions;
	}

	@Override
	public Double calculateProfitFromPeriod(Date beginDate, Date endDate) {
		QTransactionEntity transactionEntity = QTransactionEntity.transactionEntity;
		QProductEntity productEntity = QProductEntity.productEntity;
		QTransactionProductEntity transactionProductEntity = QTransactionProductEntity.transactionProductEntity;

		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		Double result = query
				.select((transactionProductEntity.numberOfBoughtItems.multiply(productEntity.price)
						.multiply((productEntity.margin).divide(100))).doubleValue().sum())
				.from(transactionEntity).join(transactionEntity.products, transactionProductEntity)
				.join(transactionProductEntity.product, productEntity)
				.where(transactionEntity.date.between(beginDate, endDate)).fetchOne();
		return result != null ? result : 0D;
	}

	@Override
	public double calculateTotalCostOfTransaction(Long customerId) {
		JPAQuery<Double> query = new JPAQuery(entityManager);
		QTransactionEntity transactionEntity = QTransactionEntity.transactionEntity;
		QProductEntity productEntity = QProductEntity.productEntity;
		QCustomerEntity customerEntity = QCustomerEntity.customerEntity;
		QTransactionProductEntity transactionProductEntity = QTransactionProductEntity.transactionProductEntity;

		double totalCost = query
				.select((productEntity.price.multiply(transactionProductEntity.numberOfBoughtItems)).sum())
				.from(transactionEntity).join(transactionEntity.customer, customerEntity)
				.join(transactionEntity.products, transactionProductEntity)
				.join(transactionProductEntity.product, productEntity).where(customerEntity.id.eq(customerId))
				.fetchOne();

		return totalCost;
	}

}
