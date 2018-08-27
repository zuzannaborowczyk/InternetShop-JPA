package com.capgemini.mappers;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.domain.ProductEntity;
import com.capgemini.domain.TransactionProductEntity;
import com.capgemini.types.ProductTO;

public class ProductMapper {

	public static ProductTO mapToTO(ProductEntity productEntity) {
		ProductTO productTO = new ProductTO();
		productTO.setId(productEntity.getId());
		productTO.setProductName(productEntity.getProductName());
		productTO.setMargin(productEntity.getMargin());
		productTO.setPrice(productEntity.getPrice());
		productTO.setWeight(productEntity.getWeight());
		productTO.setVersion(productEntity.getVersion());
		productTO.setTransactionIdList(mapEntityListToLong(productEntity.getTransactions()));
		return productTO;
	}

	private static List<Long> mapEntityListToLong(List<TransactionProductEntity> transactionproductsList) {
		List<Long> transactionproductsListTO = new ArrayList<>();
		if (transactionproductsList == null) {
			return transactionproductsListTO;
		}

		for (TransactionProductEntity t : transactionproductsList) {
			transactionproductsListTO.add(t.getTransaction().getId());

		}

		return transactionproductsListTO;
	}

	public static List<ProductTO> mapToListTO(List<ProductEntity> productEntities) {
		List<ProductTO> productsToTO = new ArrayList<>();
		for (ProductEntity product : productEntities) {
			productsToTO.add(ProductMapper.mapToTO(product));
		}
		return productsToTO;

	}

	public static ProductEntity mapToEntity(ProductTO productTO) {
		ProductEntity productEntity = new ProductEntity();
		productEntity.setId(productTO.getId());
		productEntity.setProductName(productTO.getProductName());
		productEntity.setPrice(productTO.getPrice());
		productEntity.setMargin(productTO.getMargin());
		productEntity.setWeight(productTO.getWeight());
		productEntity.setVersion(productTO.getVersion());
		return productEntity;
	}
}
