package com.capgemini.service.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.dao.CustomerRepository;
import com.capgemini.domain.CustomerEntity;
import com.capgemini.mappers.AddressMapper;
import com.capgemini.mappers.CustomerMapper;
import com.capgemini.service.CustomerService;
import com.capgemini.types.CustomerTO;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository;
	
	@Override
	public CustomerTO saveCustomer(CustomerTO newCustomer) {
		CustomerEntity custEntity = customerRepository.save(CustomerMapper.mapToEntity(newCustomer));
		return CustomerMapper.mapToTO(custEntity);
	}

	@Override
	public void deleteCustomer(Long custId) {
		customerRepository.delete(custId);
		
	}

	@Override
	public List<CustomerTO> findAllCustomers() {
		
		return CustomerMapper.mapToListTO(customerRepository.findAll());
	}

	

	@Override
	public CustomerTO findById(Long id) {
		
		return CustomerMapper.mapToTO(customerRepository.findOne(id));
	}

	@Override
	public CustomerTO update(CustomerTO customerTO) {
		CustomerEntity custEntity = customerRepository.findOne(customerTO.getId());
		if(custEntity.getVersion()!=customerTO.getVersion()) {
			throw new OptimisticLockException();
		}
		custEntity.setFirstName(customerTO.getFirstName());
		custEntity.setLastName(customerTO.getLastName());
		custEntity.setPhoneNumber(customerTO.getPhoneNumber());
		custEntity.setBirthdate(customerTO.getBirthdate());
		custEntity.setEmail(customerTO.getEmail());
		custEntity.setAddress(AddressMapper.addressToEntity(customerTO.getAddressTO()));
		
		customerRepository.save(custEntity);
		return CustomerMapper.mapToTO(custEntity);
	}

	@Override
	public List<CustomerTO> findThreeCustomersWhoSpendTheMostInPeriod(Date beginDate, Date endDate) {
		List<CustomerEntity> topThreeCustomers = customerRepository.findThreeCustomersWhoSpendTheMostInPeriod(beginDate,
				endDate);
		return CustomerMapper.mapToListTO(topThreeCustomers);
	}

}
