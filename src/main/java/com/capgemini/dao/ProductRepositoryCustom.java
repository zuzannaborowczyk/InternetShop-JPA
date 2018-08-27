package com.capgemini.dao;

import java.util.List;

import com.capgemini.domain.ProductEntity;
import com.capgemini.domain.Status;
import com.capgemini.types.ProductSummaryTO;

public interface ProductRepositoryCustom {

	List<ProductEntity> findTenBestSellersProducts(int amount);
	
	public List<ProductSummaryTO> findListOfProductsInPreparation(Status transactionStatus);
}
