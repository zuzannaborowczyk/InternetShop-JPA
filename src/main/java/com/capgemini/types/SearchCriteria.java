package com.capgemini.types;

import java.util.Date;

import com.capgemini.domain.CustomerEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SearchCriteria {

	String customerName;
	Date beginDate;
	Date endDate;
	String productName;
	Double transactionCost;
}
