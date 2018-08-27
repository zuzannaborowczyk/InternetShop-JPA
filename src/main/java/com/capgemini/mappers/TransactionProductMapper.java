package com.capgemini.mappers;

import com.capgemini.domain.TransactionProductEntity;
import com.capgemini.types.TransactionProductTO;

public class TransactionProductMapper {

	public static TransactionProductTO mapToTO(TransactionProductEntity transactionProductEntity) {
		TransactionProductTO transactionProductTO = new TransactionProductTO();
		transactionProductTO.setId(transactionProductEntity.getId());
		transactionProductTO.setNumberOfBoughtItems(transactionProductEntity.getNumberOfBoughtItems());
		transactionProductTO.setProductId(transactionProductEntity.getProduct().getId());
		transactionProductTO.setTransactionId(transactionProductEntity.getTransaction().getId());
		transactionProductTO.setVersion(transactionProductEntity.getVersion());
		return transactionProductTO;
	}
	public static TransactionProductEntity mapToEntity(TransactionProductTO transactionProductTO) {
		TransactionProductEntity transactionProductEntity = new TransactionProductEntity();
		transactionProductEntity.setId(transactionProductTO.getId());
		transactionProductEntity.setNumberOfBoughtItems(transactionProductTO.getNumberOfBoughtItems());
		transactionProductEntity.setVersion(transactionProductTO.getVersion());
		return transactionProductEntity;
	}
}
