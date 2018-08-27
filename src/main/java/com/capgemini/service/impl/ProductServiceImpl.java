package com.capgemini.service.impl;

import java.util.List;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.dao.ProductRepository;
import com.capgemini.domain.ProductEntity;
import com.capgemini.domain.Status;
import com.capgemini.mappers.ProductMapper;
import com.capgemini.service.ProductService;
import com.capgemini.types.ProductSummaryTO;
import com.capgemini.types.ProductTO;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;
	
	@Override
	public ProductTO saveProduct(ProductTO newProduct) {
		ProductEntity prodEntity = productRepository.save(ProductMapper.mapToEntity(newProduct));
		return ProductMapper.mapToTO(prodEntity);
	}

	@Override
	public void deleteProduct(Long prodIdDelete) {
		productRepository.delete(prodIdDelete);
		
	}

	@Override
	public List<ProductTO> findAllProducts() {
		return ProductMapper.mapToListTO(productRepository.findAll());
	}

	@Override
	public ProductTO update(ProductTO productTO) {
		ProductEntity productEntity = productRepository.findOne(ProductMapper.mapToEntity(productTO).getId());
		if(productEntity.getVersion() != productTO.getVersion()) {
			throw new OptimisticLockException();
		}
		productEntity.setMargin(productTO.getMargin());
		productEntity.setPrice(productTO.getPrice());
		productEntity.setProductName(productTO.getProductName());
		productEntity.setWeight(productTO.getWeight());
		productRepository.save(productEntity);
		return ProductMapper.mapToTO(productEntity);
	}

	@Override
	public ProductTO findById(Long id) {
		return ProductMapper.mapToTO(productRepository.findOne(id));
	}

	@Override
	public List<ProductTO> findTenBestSellersProducts(int numberOfBoughtItems) {
		List<ProductEntity> bestSellers = productRepository.findTenBestSellersProducts(numberOfBoughtItems);
		return ProductMapper.mapToListTO(bestSellers);
	}

	@Override
	public List<ProductSummaryTO> findListOfProductsInPreparation(Status status) {
		return productRepository.findListOfProductsInPreparation(status);
	
	}

}
