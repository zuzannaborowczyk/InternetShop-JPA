package com.capgemini.service;

import java.util.Date;
import java.util.List;

import com.capgemini.domain.CustomerEntity;
import com.capgemini.types.CustomerTO;

public interface CustomerService {

	CustomerTO saveCustomer(CustomerTO newCustomer);

	void deleteCustomer(Long custId);

	List<CustomerTO> findAllCustomers();

	CustomerTO update(CustomerTO customerTO);

	CustomerTO findById(Long id);
	
	List<CustomerTO> findThreeCustomersWhoSpendTheMostInPeriod(Date beginDate, Date endDate);


}
