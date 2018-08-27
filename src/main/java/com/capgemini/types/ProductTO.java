package com.capgemini.types;

import java.util.List;

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
public class ProductTO {

	private Long id;
	private Double price;
	private Long version;
	private String productName;
	private Double weight;
	private Double margin;
	private List<Long> transactionIdList;
}
