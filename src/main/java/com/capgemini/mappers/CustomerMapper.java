package com.capgemini.mappers;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.TransactionEntity;
import com.capgemini.types.CustomerTO;

public class CustomerMapper {

	public static CustomerTO mapToTO(CustomerEntity customerEntity) {
		CustomerTO customerTO = new CustomerTO();
		customerTO.setId(customerEntity.getId());
		customerTO.setVersion(customerEntity.getVersion());
		customerTO.setFirstName(customerEntity.getFirstName());
		customerTO.setLastName(customerEntity.getLastName());
		customerTO.setAddressTO(AddressMapper.addressToTO(customerEntity.getAddress()));
		customerTO.setBirthdate(customerEntity.getBirthdate());
		customerTO.setEmail(customerEntity.getEmail());
		customerTO.setPhoneNumber(customerEntity.getPhoneNumber());
		customerTO.setTransactionIdList(mapEntityListToLong(customerEntity.getTransactions()));
		return customerTO;
	}

	public static List<Long> mapEntityListToLong(List<TransactionEntity> transactionsList) {
		List<Long> transactionsListTO = new ArrayList<>();
		if (transactionsList == null) {
			return transactionsListTO;
		}

		for (TransactionEntity t : transactionsList) {
			transactionsListTO.add(t.getId());

		}

		return transactionsListTO;
	}
	
	public static List<CustomerTO> mapToListTO(List<CustomerEntity> customerEntities) {
		List<CustomerTO> customersToTO = new ArrayList<>();
		for (CustomerEntity customer : customerEntities) {
			customersToTO.add(CustomerMapper.mapToTO(customer));
		}
		return customersToTO;

	}
	
	

	public static CustomerEntity mapToEntity(CustomerTO customerTO) {
		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setId(customerTO.getId());
		customerEntity.setFirstName(customerTO.getFirstName());
		customerEntity.setLastName(customerTO.getLastName());
		customerEntity.setEmail(customerTO.getEmail());
		customerEntity.setAddress(AddressMapper.addressToEntity(customerTO.getAddressTO()));
		customerEntity.setBirthdate(customerTO.getBirthdate());
		customerEntity.setPhoneNumber(customerTO.getPhoneNumber());
		customerEntity.setVersion(customerTO.getVersion());
		return customerEntity;
	}
}
