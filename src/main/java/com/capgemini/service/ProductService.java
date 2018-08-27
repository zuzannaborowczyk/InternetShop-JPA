package com.capgemini.service;

import java.util.List;

import com.capgemini.domain.ProductEntity;
import com.capgemini.domain.Status;
import com.capgemini.types.ProductSummaryTO;
import com.capgemini.types.ProductTO;

public interface ProductService {

	ProductTO saveProduct(ProductTO newProduct);

	void deleteProduct(Long prodIdDelete);

	List<ProductTO> findAllProducts();

	ProductTO update(ProductTO productTO);

	ProductTO findById(Long id);
	
	List<ProductTO> findTenBestSellersProducts(int numberOfBoughtItems);
	
	List<ProductSummaryTO> findListOfProductsInPreparation(Status status);


}
