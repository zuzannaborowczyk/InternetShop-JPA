package com.capgemini.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

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
@Entity
public class ProductEntity extends AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	@Column(nullable = false, precision = 2)
	private Double price;
	@Column(nullable = false, length = 50)
	private String productName;
	@Column(nullable = false, precision = 2)
	private Double weight;
	@Column(nullable = false, precision = 2)
	private Double margin;
	@OneToMany(mappedBy ="product")
	private List<TransactionProductEntity> transactions;
	
	@Version 
	private Long version;
}
