package com.capgemini.mappers;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.ProductEntity;
import com.capgemini.domain.TransactionEntity;
import com.capgemini.domain.TransactionProductEntity;
import com.capgemini.types.TransactionTO;

public class TransactionMapper {
	public static TransactionTO mapToTO(TransactionEntity transactionEntity) {
		TransactionTO transactionTO = new TransactionTO();
		transactionTO.setId(transactionEntity.getId());
		transactionTO.setDate(transactionEntity.getDate());
		transactionTO.setStatus(transactionEntity.getStatus());
		transactionTO.setVersion(transactionEntity.getVersion());
		transactionTO.setCustomerId(transactionEntity.getCustomer().getId());
		transactionTO.setProductIdList(mapEntityListToLong(transactionEntity.getProducts()));
		return transactionTO;
	}

	private static List<Long> mapEntityListToLong(List<TransactionProductEntity> transactionproductsList) {
		List<Long> transactionproductsListTO = new ArrayList<>();
		if (transactionproductsList == null) {
			return transactionproductsListTO;
		}

		for (TransactionProductEntity t : transactionproductsList) {
			transactionproductsListTO.add(t.getProduct().getId());
			

		}

		return transactionproductsListTO;
	}

	public static List<TransactionTO> mapToListTO(List<TransactionEntity> transactionEntities) {
		List<TransactionTO> transactionsToTO = new ArrayList<>();
		for (TransactionEntity transaction : transactionEntities) {
			transactionsToTO.add(TransactionMapper.mapToTO(transaction));
		}
		return transactionsToTO;

	}

	public static TransactionEntity mapToEntity(TransactionTO transactionTO, CustomerEntity foundCustomer, List<TransactionProductEntity> foundProducts) {
		TransactionEntity transactionEntity = new TransactionEntity();
		transactionEntity.setId(transactionTO.getId());
		transactionEntity.setDate(transactionTO.getDate());
		transactionEntity.setVersion(transactionTO.getVersion());
		transactionEntity.setStatus(transactionTO.getStatus());
		transactionEntity.setCustomer(foundCustomer);
		transactionEntity.setProducts(foundProducts);
		return transactionEntity;
	}
}
