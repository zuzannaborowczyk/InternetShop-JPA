package com.capgemini.types;

import java.util.Date;
import java.util.List;

import com.capgemini.domain.Status;

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
public class TransactionTO {

	private Long id;
	private Date date;
	
	private Status status;
	private Long version;
	private Long customerId;
	
	private List<Long> productIdList;
}
