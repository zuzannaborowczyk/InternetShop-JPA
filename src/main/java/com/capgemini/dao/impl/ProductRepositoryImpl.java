package com.capgemini.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.capgemini.dao.ProductRepositoryCustom;
import com.capgemini.domain.ProductEntity;
import com.capgemini.domain.QProductEntity;
import com.capgemini.domain.QTransactionEntity;
import com.capgemini.domain.QTransactionProductEntity;
import com.capgemini.domain.Status;
import com.capgemini.types.ProductSummaryTO;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<ProductEntity> findTenBestSellersProducts(int numberOfBoughtItems) {
		QProductEntity productEntity = QProductEntity.productEntity;
		QTransactionProductEntity transactionProductEntity = QTransactionProductEntity.transactionProductEntity;
		JPAQuery<ProductEntity> query = new JPAQuery(entityManager);

		List<ProductEntity> bestSellers = query.from(productEntity).select(productEntity)
				.join(productEntity.transactions, transactionProductEntity).where().groupBy(productEntity.id)
				.orderBy(transactionProductEntity.numberOfBoughtItems.sum().desc()).limit(numberOfBoughtItems).fetch();

		return bestSellers;

	}

	@Override
	public List<ProductSummaryTO> findListOfProductsInPreparation(Status status) {
		JPAQuery<ProductEntity> query = new JPAQuery(entityManager);
		QProductEntity productEntity = QProductEntity.productEntity;
		QTransactionEntity transactionEntity = QTransactionEntity.transactionEntity;
		QTransactionProductEntity transactionProductEntity = QTransactionProductEntity.transactionProductEntity;

		List<Tuple> tuples = query.select(productEntity.productName, transactionProductEntity.numberOfBoughtItems.sum())
				.from(productEntity).innerJoin(productEntity.transactions, transactionProductEntity)
				.innerJoin(transactionProductEntity.transaction, transactionEntity)
				.where(transactionEntity.status.eq(Status.IN_PREPARATION)).groupBy(productEntity.productName)
				.orderBy(transactionProductEntity.numberOfBoughtItems.sum().desc()).fetch();

		List<ProductSummaryTO> results = new ArrayList<>();
		for (Tuple tuple : tuples) {
			results.add(new ProductSummaryTO(tuple.get(productEntity.productName),
					tuple.get(transactionProductEntity.numberOfBoughtItems.sum())));
		}

		return results;
	}

}
