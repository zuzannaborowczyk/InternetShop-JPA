package com.capgemini.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.capgemini.dao.ProductRepository;
import com.capgemini.domain.ProductEntity;
import com.capgemini.domain.Status;
import com.capgemini.types.AddressTO;
import com.capgemini.types.CustomerTO;
import com.capgemini.types.ProductSummaryTO;
import com.capgemini.types.ProductTO;
import com.capgemini.types.TransactionProductTO;
import com.capgemini.types.TransactionTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

	@Autowired
	ProductService productService;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	TransactionService transactionService;

	@Autowired
	CustomerService customerService;

	@Test
	public void shouldAddProduct() {

		// given
		ProductTO blueSocks = ProductTO.builder().productName("Soft Socks").price(4.99).margin(2.5).weight(0.1).build();
		// when
		ProductTO savedBlueSocks = productService.saveProduct(blueSocks);
		long searchedId = savedBlueSocks.getId();
		ProductTO foundBlueSocks = productService.findById(searchedId);
		// then
		assertEquals("Soft Socks", foundBlueSocks.getProductName());
	}

	@Test
	public void shouldDeleteProduct() {

		// given
		int previouslyAddedProducts = productService.findAllProducts().size();
		ProductTO blueSocks = ProductTO.builder().productName("Soft Socks").price(4.99).margin(2.5).weight(0.1).build();
		ProductTO redSocks = ProductTO.builder().productName("Soft Socks").price(3.99).margin(2.5).weight(0.1).build();
		ProductTO savedBlueSocks = productService.saveProduct(blueSocks);
		ProductTO savedRedSocks = productService.saveProduct(redSocks);

		// when
		productService.deleteProduct(savedBlueSocks.getId());
		List<ProductTO> allProducts = productService.findAllProducts();
		// then
		assertEquals(1+previouslyAddedProducts, allProducts.size());
		

	}

	@Test
	public void shouldFindAllProducts() {

		// given
		int previouslyAddedProducts = productService.findAllProducts().size();
		ProductTO blueSocks = ProductTO.builder().productName("Soft Socks").price(4.99).margin(2.5).weight(0.1).build();
		ProductTO redSocks = ProductTO.builder().productName("Soft Socks").price(3.99).margin(2.5).weight(0.1).build();
		ProductTO savedBlueSocks = productService.saveProduct(blueSocks);
		ProductTO savedRedSocks = productService.saveProduct(redSocks);
		// when
		List<ProductTO> allProducts = productService.findAllProducts();
		// then
		assertThat(allProducts.size()).isEqualTo(2 + previouslyAddedProducts);
	}

	@Test
	public void shouldFindProductById() {

		// given
		ProductTO blueSocks = ProductTO.builder().productName("Soft Socks").price(4.99).margin(2.5).weight(0.1).build();
		ProductTO savedBlueSocks = productService.saveProduct(blueSocks);
		// when
		ProductTO foundBlueSocks = productService.findById(savedBlueSocks.getId());
		// then
		assertThat(savedBlueSocks.getMargin()).isEqualTo(foundBlueSocks.getMargin());

	}

	@Test
	public void shouldUpdateProduct() {

		// given
		int previouslyAddedProducts = productService.findAllProducts().size();
		ProductTO blueSocks = ProductTO.builder().productName("Soft Socks").price(4.99).margin(2.5).weight(0.1).build();
		ProductTO savedBlueSocks = productService.saveProduct(blueSocks);
		Double newMargin = 2.2;
		// when
		savedBlueSocks.setMargin(newMargin);
		ProductTO updatedSocks = productService.update(savedBlueSocks);
		List<ProductTO> allProducts = productService.findAllProducts();
		// then
		assertThat(updatedSocks.getMargin()).isEqualTo(newMargin);
		assertEquals(updatedSocks.getId(), savedBlueSocks.getId());
		assertThat(productService.findById(updatedSocks.getId()).getMargin()).isEqualTo(newMargin);
		assertThat(allProducts.size()).isEqualTo(1 + previouslyAddedProducts);

	}

	@Test
	public void shouldHaveCreationAndUpdateDate() throws InterruptedException {

		// given
		ProductTO blueSocks = ProductTO.builder().productName("Soft Socks").price(4.99).margin(2.5).weight(0.1).build();
		ProductTO savedBlueSocks = productService.saveProduct(blueSocks);
		Double newMargin = 2.2;
		// when
		savedBlueSocks.setMargin(newMargin);
		Date creationDate1 = productRepository.findOne(savedBlueSocks.getId()).getCreationDate();
		TimeUnit.SECONDS.sleep(1);
		productService.update(savedBlueSocks);
		ProductEntity foundBlueSocks = productRepository.findOne(savedBlueSocks.getId());
		Date creationDate2 = productRepository.findOne(foundBlueSocks.getId()).getCreationDate();

		// then
		assertThat(foundBlueSocks.getCreationDate()).isNotNull();
		assertThat(creationDate1).isEqualTo(creationDate2);
		assertThat(foundBlueSocks.getUpdateDate()).isNotNull();

	}

	@Test
	public void shouldFindTenBestSellersProducts() throws Exception {
		// given
		Date birthdate = java.sql.Date.valueOf("1950-11-01");

		AddressTO address = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456").city("Poznan")
				.build();
		CustomerTO tinaTurner = CustomerTO.builder().firstName("Tina").lastName("Turner").birthdate(birthdate)
				.addressTO(address).email("tina.turner@gmail.com").phoneNumber("876543234").build();
		CustomerTO savedTina = customerService.saveCustomer(tinaTurner);
		CustomerTO eddieMurphy = CustomerTO.builder().firstName("Eddie").lastName("Murphy").birthdate(birthdate)
				.addressTO(address).email("tina.turner@gmail.com").phoneNumber("876543234").build();
		CustomerTO savedEddie = customerService.saveCustomer(eddieMurphy);
		CustomerTO dorisDay = CustomerTO.builder().firstName("Doris").lastName("Day").birthdate(birthdate)
				.addressTO(address).email("tina.turner@gmail.com").phoneNumber("876543234").build();
		CustomerTO savedDoris = customerService.saveCustomer(dorisDay);
		CustomerTO johnTravolta = CustomerTO.builder().firstName("John").lastName("Travolta").birthdate(birthdate)
				.addressTO(address).email("tina.turner@gmail.com").phoneNumber("876543234").build();
		CustomerTO savedJohn = customerService.saveCustomer(johnTravolta);
		CustomerTO andreaBocelli = CustomerTO.builder().firstName("Andrea").lastName("Bocelli").birthdate(birthdate)
				.addressTO(address).email("tina.turner@gmail.com").phoneNumber("876543234").build();
		CustomerTO savedAndrea = customerService.saveCustomer(andreaBocelli);

		TransactionTO tinasTransaction = TransactionTO.builder().customerId(savedTina.getId()).status(Status.EXECUTED)
				.build();
		TransactionTO eddiesTransaction = TransactionTO.builder().customerId(savedEddie.getId()).status(Status.EXECUTED)
				.build();
		TransactionTO dorisTransaction = TransactionTO.builder().customerId(savedDoris.getId()).status(Status.EXECUTED)
				.build();
		TransactionTO johnsTransaction = TransactionTO.builder().customerId(savedJohn.getId()).status(Status.EXECUTED)
				.build();
		TransactionTO andreasTransaction = TransactionTO.builder().customerId(savedAndrea.getId())
				.status(Status.EXECUTED).build();
		TransactionTO createdTinasTransaction = transactionService.createTransaction(tinasTransaction);
		TransactionTO createdEddiesTransaction = transactionService.createTransaction(eddiesTransaction);
		TransactionTO createdDorisTransaction = transactionService.createTransaction(dorisTransaction);
		TransactionTO createdJohnsTransaction = transactionService.createTransaction(johnsTransaction);
		TransactionTO createdAndreasTransaction = transactionService.createTransaction(andreasTransaction);

		ProductTO blueSocks = ProductTO.builder().productName("Soft Socks").price(10.0).margin(2.5).weight(0.1).build();
		ProductTO microwave = ProductTO.builder().productName("Microwave").price(1000.0).margin(2.5).weight(0.1)
				.build();
		ProductTO fridge = ProductTO.builder().productName("Fridge").price(2000.0).margin(2.5).weight(0.1).build();
		ProductTO savedBlueSocks = productService.saveProduct(blueSocks);
		ProductTO savedMicrowave = productService.saveProduct(microwave);
		ProductTO savedFridge = productService.saveProduct(fridge);

		TransactionTO tinasProducts = transactionService.addProductToTransaction(savedBlueSocks,
				createdTinasTransaction);
		TransactionTO eddiesProducts = transactionService.addProductToTransaction(savedFridge, createdEddiesTransaction);
		TransactionTO dorisProducts = transactionService.addProductToTransaction(savedMicrowave,
				createdDorisTransaction);
		TransactionTO johnsProducts = transactionService.addProductToTransaction(savedFridge, createdJohnsTransaction);
		TransactionTO andreasProducts = transactionService.addProductToTransaction(savedBlueSocks,
				createdAndreasTransaction);

		TransactionProductTO tinasOrder1 = TransactionProductTO.builder().numberOfBoughtItems(1)
				.productId(savedBlueSocks.getId()).transactionId(createdTinasTransaction.getId()).build();
		TransactionProductTO tinasOrder2 = TransactionProductTO.builder().numberOfBoughtItems(1)
				.productId(savedBlueSocks.getId()).transactionId(createdTinasTransaction.getId()).build();
		TransactionProductTO tinasOrder3 = TransactionProductTO.builder().numberOfBoughtItems(1)
				.productId(savedBlueSocks.getId()).transactionId(createdTinasTransaction.getId()).build();
		TransactionProductTO eddiesOrder = TransactionProductTO.builder().numberOfBoughtItems(1)
				.productId(savedFridge.getId()).transactionId(createdEddiesTransaction.getId()).build();
		TransactionProductTO dorisOrder = TransactionProductTO.builder().numberOfBoughtItems(2)
				.productId(savedMicrowave.getId()).transactionId(createdDorisTransaction.getId()).build();
		TransactionProductTO johnsOrder1 = TransactionProductTO.builder().numberOfBoughtItems(1)
				.productId(savedFridge.getId()).transactionId(createdJohnsTransaction.getId()).build();
		TransactionProductTO johnsOrder2 = TransactionProductTO.builder().numberOfBoughtItems(1)
				.productId(savedFridge.getId()).transactionId(createdJohnsTransaction.getId()).build();
		TransactionProductTO andreasOrder = TransactionProductTO.builder().numberOfBoughtItems(1)
				.productId(savedBlueSocks.getId()).transactionId(createdAndreasTransaction.getId()).build();

		TransactionProductTO savedTinasOrder1 = transactionService.saveTransactionProduct(tinasOrder1);
		TransactionProductTO savedTinasOrder2 = transactionService.saveTransactionProduct(tinasOrder2);
		TransactionProductTO savedTinasOrder3 = transactionService.saveTransactionProduct(tinasOrder3);
		TransactionProductTO savedEddiesOrder = transactionService.saveTransactionProduct(eddiesOrder);
		TransactionProductTO savedDorisOrder = transactionService.saveTransactionProduct(dorisOrder);
		TransactionProductTO savedJohnsOrder1 = transactionService.saveTransactionProduct(johnsOrder1);
		TransactionProductTO savedJohnsOrder2 = transactionService.saveTransactionProduct(johnsOrder2);
		TransactionProductTO savedAndresOrder = transactionService.saveTransactionProduct(andreasOrder);
		// when
		List<ProductTO> bestSellers = productService.findTenBestSellersProducts(2);
		// then
		assertNotNull(bestSellers);
		assertEquals(2, bestSellers.size());
		assertEquals("Soft Socks", bestSellers.get(0).getProductName());
		assertEquals("Fridge", bestSellers.get(1).getProductName());
	}

	@Test
	public void shouldFindListOfProductsInPreparation() throws Exception {

		// given
		Date birthdate = java.sql.Date.valueOf("1950-11-01");

		AddressTO address = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456").city("Poznan")
				.build();
		CustomerTO tinaTurner = CustomerTO.builder().firstName("Tina").lastName("Turner").birthdate(birthdate)
				.addressTO(address).email("tina.turner@gmail.com").phoneNumber("876543234").build();
		CustomerTO savedTina = customerService.saveCustomer(tinaTurner);
		CustomerTO eddieMurphy = CustomerTO.builder().firstName("Eddie").lastName("Murphy").birthdate(birthdate)
				.addressTO(address).email("tina.turner@gmail.com").phoneNumber("876543234").build();
		CustomerTO savedEddie = customerService.saveCustomer(eddieMurphy);
		CustomerTO dorisDay = CustomerTO.builder().firstName("Doris").lastName("Day").birthdate(birthdate)
				.addressTO(address).email("tina.turner@gmail.com").phoneNumber("876543234").build();
		CustomerTO savedDoris = customerService.saveCustomer(dorisDay);
		CustomerTO johnTravolta = CustomerTO.builder().firstName("John").lastName("Travolta").birthdate(birthdate)
				.addressTO(address).email("tina.turner@gmail.com").phoneNumber("876543234").build();
		CustomerTO savedJohn = customerService.saveCustomer(johnTravolta);
		CustomerTO andreaBocelli = CustomerTO.builder().firstName("Andrea").lastName("Bocelli").birthdate(birthdate)
				.addressTO(address).email("tina.turner@gmail.com").phoneNumber("876543234").build();
		CustomerTO savedAndrea = customerService.saveCustomer(andreaBocelli);

		TransactionTO tinasTransaction = TransactionTO.builder().customerId(savedTina.getId()).status(Status.EXECUTED)
				.build();
		TransactionTO eddiesTransaction = TransactionTO.builder().customerId(savedEddie.getId()).status(Status.IN_PREPARATION)
				.build();
		TransactionTO dorisTransaction = TransactionTO.builder().customerId(savedDoris.getId()).status(Status.IN_PREPARATION)
				.build();
		TransactionTO johnsTransaction = TransactionTO.builder().customerId(savedJohn.getId()).status(Status.EXECUTED)
				.build();
		TransactionTO andreasTransaction = TransactionTO.builder().customerId(savedAndrea.getId())
				.status(Status.IN_PREPARATION).build();
		TransactionTO createdTinasTransaction = transactionService.createTransaction(tinasTransaction);
		TransactionTO createdEddiesTransaction = transactionService.createTransaction(eddiesTransaction);
		TransactionTO createdDorisTransaction = transactionService.createTransaction(dorisTransaction);
		TransactionTO createdJohnsTransaction = transactionService.createTransaction(johnsTransaction);
		TransactionTO createdAndreasTransaction = transactionService.createTransaction(andreasTransaction);

		ProductTO blueSocks = ProductTO.builder().productName("Soft Socks").price(10.0).margin(2.5).weight(0.1).build();
		ProductTO microwave = ProductTO.builder().productName("Microwave").price(1000.0).margin(2.5).weight(0.1)
				.build();
		ProductTO fridge = ProductTO.builder().productName("Fridge").price(2000.0).margin(2.5).weight(0.1).build();
		ProductTO savedBlueSocks = productService.saveProduct(blueSocks);
		ProductTO savedMicrowave = productService.saveProduct(microwave);
		ProductTO savedFridge = productService.saveProduct(fridge);

		TransactionTO tinasProducts = transactionService.addProductToTransaction(savedBlueSocks,
				createdTinasTransaction);
		TransactionTO eddiesProducts = transactionService.addProductToTransaction(savedFridge, createdEddiesTransaction);
		TransactionTO dorisProducts = transactionService.addProductToTransaction(savedMicrowave,
				createdDorisTransaction);
		TransactionTO johnsProducts = transactionService.addProductToTransaction(savedFridge, createdJohnsTransaction);
		TransactionTO andreasProducts = transactionService.addProductToTransaction(savedBlueSocks,
				createdAndreasTransaction);

		TransactionProductTO tinasOrder1 = TransactionProductTO.builder().numberOfBoughtItems(1)
				.productId(savedBlueSocks.getId()).transactionId(createdTinasTransaction.getId()).build();
		TransactionProductTO tinasOrder2 = TransactionProductTO.builder().numberOfBoughtItems(1)
				.productId(savedBlueSocks.getId()).transactionId(createdTinasTransaction.getId()).build();
		TransactionProductTO tinasOrder3 = TransactionProductTO.builder().numberOfBoughtItems(1)
				.productId(savedBlueSocks.getId()).transactionId(createdTinasTransaction.getId()).build();
		TransactionProductTO eddiesOrder = TransactionProductTO.builder().numberOfBoughtItems(1)
				.productId(savedFridge.getId()).transactionId(createdEddiesTransaction.getId()).build();
		TransactionProductTO dorisOrder = TransactionProductTO.builder().numberOfBoughtItems(2)
				.productId(savedMicrowave.getId()).transactionId(createdDorisTransaction.getId()).build();
		TransactionProductTO johnsOrder1 = TransactionProductTO.builder().numberOfBoughtItems(1)
				.productId(savedFridge.getId()).transactionId(createdJohnsTransaction.getId()).build();
		TransactionProductTO johnsOrder2 = TransactionProductTO.builder().numberOfBoughtItems(1)
				.productId(savedFridge.getId()).transactionId(createdJohnsTransaction.getId()).build();
		TransactionProductTO andreasOrder = TransactionProductTO.builder().numberOfBoughtItems(1)
				.productId(savedBlueSocks.getId()).transactionId(createdAndreasTransaction.getId()).build();

		TransactionProductTO savedTinasOrder1 = transactionService.saveTransactionProduct(tinasOrder1);
		TransactionProductTO savedTinasOrder2 = transactionService.saveTransactionProduct(tinasOrder2);
		TransactionProductTO savedTinasOrder3 = transactionService.saveTransactionProduct(tinasOrder3);
		TransactionProductTO savedEddiesOrder = transactionService.saveTransactionProduct(eddiesOrder);
		TransactionProductTO savedDorisOrder = transactionService.saveTransactionProduct(dorisOrder);
		TransactionProductTO savedJohnsOrder1 = transactionService.saveTransactionProduct(johnsOrder1);
		TransactionProductTO savedJohnsOrder2 = transactionService.saveTransactionProduct(johnsOrder2);
		TransactionProductTO savedAndresOrder = transactionService.saveTransactionProduct(andreasOrder);
		// when
		List<ProductSummaryTO> productsInPrep = productService
				.findListOfProductsInPreparation(Status.IN_PREPARATION);
		// then
		assertNotNull(productsInPrep);
		assertEquals(3, productsInPrep.size());

	}
}
