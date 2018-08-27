package com.capgemini.dao;

import java.sql.Date;
import java.util.List;

import com.capgemini.domain.Status;
import com.capgemini.domain.TransactionEntity;
import com.capgemini.types.SearchCriteria;

public interface TransactionRepositoryCustom {

	List<TransactionEntity> findTransactionsBySearchCriteria(SearchCriteria searchCriteria);

	Double calculateProfitFromPeriod(Date beginDate, Date endDate);

	double calculateTotalCostOfTransaction(Long customerId);

	double calculateTotalCostOfTransactionsWithStatus(Status status);
}
