package com.capgemini.service.impl;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.dao.CustomerRepository;
import com.capgemini.dao.ProductRepository;
import com.capgemini.dao.TransactionProductRepository;
import com.capgemini.dao.TransactionRepository;
import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.ProductEntity;
import com.capgemini.domain.Status;
import com.capgemini.domain.TransactionEntity;
import com.capgemini.domain.TransactionProductEntity;
import com.capgemini.exceptions.InvalidTransactionException;
import com.capgemini.mappers.TransactionMapper;
import com.capgemini.mappers.TransactionProductMapper;
import com.capgemini.service.TransactionService;
import com.capgemini.types.ProductTO;
import com.capgemini.types.SearchCriteria;
import com.capgemini.types.TransactionProductTO;
import com.capgemini.types.TransactionTO;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	TransactionProductRepository transactionProductRepository;

	@Override
	// TODO ymienic typ wyjatku
	public TransactionTO createTransaction(TransactionTO newTransactionTO) {
		CustomerEntity foundCustomer = customerRepository.findOne(newTransactionTO.getCustomerId());
		java.util.Date date = new java.util.Date();
		Date dt = Date.valueOf("2018-08-27");//date.toString());
		TransactionEntity newTransaction = TransactionEntity.builder().customer(foundCustomer).date(newTransactionTO.getDate() == null ? dt/*new Date()*/ : newTransactionTO.getDate())
				.status(Status.IN_PREPARATION).products(new ArrayList<TransactionProductEntity>()).build();
		List<Long> productsId = newTransactionTO.getProductIdList();

		TransactionEntity createdTransaction = transactionRepository.save(newTransaction);

		return TransactionMapper.mapToTO(createdTransaction);
	}

	@Override
	public void validateTransaction(CustomerEntity customer, List<TransactionProductEntity> transactionProducts)
			throws InvalidTransactionException {
		checkIfCustomerBoughtMoreThanFiveSameProducts(transactionProducts);
		checkIfCustomerHasLessThanThreeTransactions(customer, transactionProducts);
		checkPermittedWeightOfProducts(transactionProducts);

	}

	@Override
	public void checkIfCustomerHasLessThanThreeTransactions(CustomerEntity customer,
			List<TransactionProductEntity> products) throws InvalidTransactionException {
		if (customer.getTransactions() == null || customer.getTransactions().size() < 3) {
			double totalCost = 0;
			for (TransactionProductEntity product : products) {
				totalCost = product.getProduct().getPrice() * product.getNumberOfBoughtItems();
				
				}
				if (totalCost > 5000) {
					throw new InvalidTransactionException();
			}
		}
	}

	@Override
	public void checkIfCustomerBoughtMoreThanFiveSameProducts(List<TransactionProductEntity> transactionProducts)
			throws InvalidTransactionException {

		for (TransactionProductEntity product : transactionProducts) {

			if (product.getProduct().getPrice() > 7000D) {
				if (product.getNumberOfBoughtItems() >= 5) {
					throw new InvalidTransactionException();
				}

			}
		}

	}

	@Override
	public void checkPermittedWeightOfProducts(List<TransactionProductEntity> transactionProducts) throws InvalidTransactionException {
		Double maxWeight = 25D;
		Double weightSum = 0D;
		for (TransactionProductEntity productEntity : transactionProducts) {
			weightSum = productEntity.getProduct().getWeight() * productEntity.getNumberOfBoughtItems();
			if (weightSum > maxWeight) {
				throw new InvalidTransactionException();
			}
		}
	}

	@Override
	public TransactionTO addProductToTransaction(ProductTO newProductTO, TransactionTO transaction) throws InvalidTransactionException {

		TransactionEntity foundTransaction = transactionRepository.findOne(transaction.getId());
		List<TransactionProductEntity> foundProducts = foundTransaction.getProducts();

		validateTransaction(foundTransaction.getCustomer(), foundTransaction.getProducts());

		boolean containsProduct = foundProducts.stream().anyMatch(p -> p.getProduct().getId() == newProductTO.getId());

		if (containsProduct) {

			TransactionProductEntity foundProduct = foundProducts.stream()
					.filter(p -> p.getProduct().getId() == newProductTO.getId()).findFirst().get();
			int currentNumerOfBoughtItems = foundProduct.getNumberOfBoughtItems();
			foundProduct.setNumberOfBoughtItems(currentNumerOfBoughtItems + 1);
		}
		ProductEntity foundProduct = productRepository.findOne(newProductTO.getId());
		TransactionProductEntity newProduct = TransactionProductEntity.builder().numberOfBoughtItems(1)
				.product(foundProduct).transaction(foundTransaction).build();

		TransactionProductEntity savedProduct = transactionProductRepository.save(newProduct);
		foundProducts.add(savedProduct);

		return TransactionMapper.mapToTO(foundTransaction);
	}

	@Override
	public TransactionTO finishTransaction(TransactionTO newTransaction) {
		TransactionEntity foundTransaction = transactionRepository.findOne(newTransaction.getId());
		foundTransaction.setStatus(Status.EXECUTED);

		return TransactionMapper.mapToTO(foundTransaction);
	}

	@Override
	public void deleteTransaction(TransactionTO transToDelete) {
		transactionRepository.delete(transToDelete.getId());

	}

	@Override
	public List<TransactionTO> findAllTransactions() {
		return TransactionMapper.mapToListTO(transactionRepository.findAll());
	}

	@Override
	public TransactionTO update(TransactionTO transactionTO) {
		TransactionEntity transEntity = transactionRepository.findOne(transactionTO.getId());
		if (transEntity.getVersion() != transactionTO.getVersion()) {
			throw new OptimisticLockException();
		}
		transEntity.setDate(transactionTO.getDate());
		transEntity.setStatus(transactionTO.getStatus());
		transactionRepository.save(transEntity);
		return TransactionMapper.mapToTO(transEntity);
	}

	@Override
	public TransactionTO findById(Long id) {
		return TransactionMapper.mapToTO(transactionRepository.findOne(id));

	}

	@Override
	public List<TransactionTO> findTransactionsBySearchCriteria(SearchCriteria searchCriteria) {
		List<TransactionEntity> foundTransactions = transactionRepository
				.findTransactionsBySearchCriteria(searchCriteria);
		return TransactionMapper.mapToListTO(foundTransactions);
	}

	@Override
	public double calculateTotalCostOfTransactionsWithStatus(Status status) {

		return transactionRepository.calculateTotalCostOfTransactionsWithStatus(status);
	}

	@Override
	public Double calculateProfitFromPeriod(Date beginDate, Date endDate) {

		return transactionRepository.calculateProfitFromPeriod(endDate, endDate);
	}

	@Override
	public double calculateTotalCostOfTransaction(Long customerId) {

		return transactionRepository.calculateTotalCostOfTransaction(customerId);
	}

	@Override
	public TransactionProductTO saveTransactionProduct(TransactionProductTO newTransactionProduct) {
		ProductEntity productEntity = productRepository.findOne(newTransactionProduct.getProductId());
		TransactionEntity transactionEntity = transactionRepository.findOne(newTransactionProduct.getTransactionId());

		
		TransactionProductEntity orderEntity = new TransactionProductEntity();
		orderEntity.setNumberOfBoughtItems(newTransactionProduct.getNumberOfBoughtItems());
		orderEntity.setProduct(productEntity);
		orderEntity.setTransaction(transactionEntity);

		return TransactionProductMapper.mapToTO(transactionProductRepository.save(orderEntity));
	}

}
