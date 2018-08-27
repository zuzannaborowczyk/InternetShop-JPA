package com.capgemini.types;

import java.util.Date;
import java.util.List;

import com.capgemini.types.CustomerTO.CustomerTOBuilder;

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
public class ProductSummaryTO {

	String nameOfProduct;
	int amountOfProduct;
}
