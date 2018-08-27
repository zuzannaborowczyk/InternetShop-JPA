package com.capgemini.service;

import java.sql.Date;
import java.util.List;

import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.ProductEntity;
import com.capgemini.domain.Status;
import com.capgemini.domain.TransactionEntity;
import com.capgemini.domain.TransactionProductEntity;
import com.capgemini.exceptions.InvalidTransactionException;
import com.capgemini.types.CustomerTO;
import com.capgemini.types.ProductTO;
import com.capgemini.types.SearchCriteria;
import com.capgemini.types.TransactionProductTO;
import com.capgemini.types.TransactionTO;

public interface TransactionService {

	TransactionTO createTransaction(TransactionTO newTransaction);

	void validateTransaction(CustomerEntity customer, List<TransactionProductEntity> transactionProducts) throws InvalidTransactionException;

	void checkIfCustomerHasLessThanThreeTransactions(CustomerEntity customer, List<TransactionProductEntity> transactionProducts) throws InvalidTransactionException;

	void checkIfCustomerBoughtMoreThanFiveSameProducts(List<TransactionProductEntity> transactionProducts) throws InvalidTransactionException;
	
	void checkPermittedWeightOfProducts(List<TransactionProductEntity> products) throws InvalidTransactionException;

	TransactionTO addProductToTransaction(ProductTO newProduct, TransactionTO newTransaction) throws InvalidTransactionException;

	TransactionTO finishTransaction(TransactionTO newTransaction);

	void deleteTransaction(TransactionTO transToDelete);

	List<TransactionTO> findAllTransactions();

	TransactionTO update(TransactionTO transactionTO);

	TransactionTO findById(Long id);
	
	List<TransactionTO> findTransactionsBySearchCriteria(SearchCriteria searchCriteria);
	
	double calculateTotalCostOfTransactionsWithStatus(Status status);
	
	Double calculateProfitFromPeriod(Date beginDate, Date endDate);
	
	double calculateTotalCostOfTransaction(Long customerId);
	
	TransactionProductTO saveTransactionProduct(TransactionProductTO newTransactionProduct);





}
