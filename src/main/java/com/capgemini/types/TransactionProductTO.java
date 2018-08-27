package com.capgemini.types;

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
public class TransactionProductTO {

	private Long id;
	private int numberOfBoughtItems;
	
	private Long version;
	private Long transactionId;
	
	
	private Long productId;
}
