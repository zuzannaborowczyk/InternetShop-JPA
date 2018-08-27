package com.capgemini.dao;

import java.util.Date;
import java.util.List;

import com.capgemini.domain.CustomerEntity;

public interface CustomerRepositoryCustom {

	List<CustomerEntity> findThreeCustomersWhoSpendTheMostInPeriod(Date beginDate, Date endDate);

}
