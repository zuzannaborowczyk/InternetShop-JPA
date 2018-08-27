package com.capgemini.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.sql.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.capgemini.domain.Status;
import com.capgemini.exceptions.InvalidTransactionException;
import com.capgemini.types.AddressTO;
import com.capgemini.types.CustomerTO;
import com.capgemini.types.ProductTO;
import com.capgemini.types.SearchCriteria;
import com.capgemini.types.TransactionProductTO;
import com.capgemini.types.TransactionTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTest {

	@Autowired
	TransactionService transactionService;

	@Autowired
	CustomerService customerService;

	@Autowired
	ProductService productService;

	@Test
	public void shouldAddTransaction() throws Exception {

		// given
		AddressTO freddiesAddress = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456")
				.city("Poznan").build();
		CustomerTO freddieMercury = CustomerTO.builder().firstName("Freddie").lastName("Mercury")
				.birthdate(Date.valueOf("1965-10-09")).addressTO(freddiesAddress).email("freddie.mercury@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedFreddie = customerService.saveCustomer(freddieMercury);
		Status currentStatus = Status.IN_DELIVERY;
		ProductTO blueSocks = ProductTO.builder().productName("Soft Socks").price(4.99).margin(2.5).weight(0.1).build();
		ProductTO redSocks = ProductTO.builder().productName("Soft Socks").price(3.99).margin(2.5).weight(0.1).build();
		ProductTO savedBlueSocks = productService.saveProduct(blueSocks);
		ProductTO savedRedSocks = productService.saveProduct(redSocks);
		TransactionTO freddiesTransaction = TransactionTO.builder().customerId(savedFreddie.getId())
				.date(Date.valueOf("2018-08-12")).build();
		// when

		TransactionTO savedFreddiesTransaction = transactionService.createTransaction(freddiesTransaction);
		TransactionTO addProductBlueSocks = transactionService.addProductToTransaction(savedBlueSocks,
				savedFreddiesTransaction);
		TransactionTO addProductRedSocks = transactionService.addProductToTransaction(savedRedSocks,
				savedFreddiesTransaction);
		long searchedId = savedFreddiesTransaction.getId();
		TransactionTO foundFreddiesTransaction = transactionService.findById(searchedId);
		CustomerTO foundCustomer = customerService.findById(savedFreddie.getId());
		ProductTO foundBlueSocks = productService.findById(savedBlueSocks.getId());
		ProductTO foundRedSocks = productService.findById(savedRedSocks.getId());
		// then
		assertTrue(foundFreddiesTransaction.getProductIdList().contains((Object) foundBlueSocks.getId()));
		assertTrue(foundFreddiesTransaction.getProductIdList().contains((Object) foundRedSocks.getId()));
		assertTrue(foundCustomer.getTransactionIdList().stream().anyMatch(b -> b == savedFreddiesTransaction.getId()));
		assertThat(customerService.findById(foundCustomer.getId()).getTransactionIdList().get(0))
				.isEqualTo(foundFreddiesTransaction.getId());
		assertThat(productService.findById(foundBlueSocks.getId()).getTransactionIdList().size()).isEqualTo(1);
		assertThat(productService.findById(foundBlueSocks.getId()).getTransactionIdList().get(0))
				.isEqualTo(foundFreddiesTransaction.getId());
		assertThat(productService.findById(foundRedSocks.getId()).getTransactionIdList().size()).isEqualTo(1);
		assertThat(productService.findById(foundRedSocks.getId()).getTransactionIdList().get(0))
				.isEqualTo(foundFreddiesTransaction.getId());

	}

	@Test
	public void shouldDeleteTransaction() throws Exception {

		// given
		int previouslyAddedTransactions = transactionService.findAllTransactions().size();
		AddressTO freddiesAddress = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456")
				.city("Poznan").build();
		CustomerTO freddieMercury = CustomerTO.builder().firstName("Freddie").lastName("Mercury")
				.birthdate(Date.valueOf("1965-10-09")).addressTO(freddiesAddress).email("freddie.mercury@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedFreddie = customerService.saveCustomer(freddieMercury);
		Status currentStatus = Status.IN_DELIVERY;
		ProductTO blueSocks = ProductTO.builder().productName("Soft Socks").price(4.99).margin(2.5).weight(0.1).build();
		ProductTO redSocks = ProductTO.builder().productName("Soft Socks").price(3.99).margin(2.5).weight(0.1).build();
		ProductTO savedBlueSocks = productService.saveProduct(blueSocks);
		ProductTO savedRedSocks = productService.saveProduct(redSocks);
		TransactionTO freddiesTransaction = TransactionTO.builder().customerId(savedFreddie.getId())
				.date(Date.valueOf("2018-08-12")).build();
		TransactionTO savedFreddiesTransaction1 = transactionService.createTransaction(freddiesTransaction);
		TransactionTO savedFreddiesTransaction2 = transactionService.createTransaction(freddiesTransaction);

		// when
		transactionService.deleteTransaction(savedFreddiesTransaction1);
		List<TransactionTO> allTransactions = transactionService.findAllTransactions();
		// then
		assertThat(allTransactions.size()).isEqualTo(1 + previouslyAddedTransactions);
		assertThat(customerService.findById(savedFreddie.getId()).getTransactionIdList()
				.contains(savedFreddiesTransaction1.getId())).isFalse();

	}

	@Test
	public void shouldUpdateTransaction() throws Exception {

		// given
		AddressTO freddiesAddress = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456")
				.city("Poznan").build();
		CustomerTO freddieMercury = CustomerTO.builder().firstName("Freddie").lastName("Mercury")
				.birthdate(Date.valueOf("1965-10-09")).addressTO(freddiesAddress).email("freddie.mercury@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedFreddie = customerService.saveCustomer(freddieMercury);
		Status currentStatus = Status.IN_DELIVERY;
		ProductTO blueSocks = ProductTO.builder().productName("Soft Socks").price(4.99).margin(2.5).weight(0.1).build();
		ProductTO redSocks = ProductTO.builder().productName("Soft Socks").price(3.99).margin(2.5).weight(0.1).build();
		ProductTO savedBlueSocks = productService.saveProduct(blueSocks);
		ProductTO savedRedSocks = productService.saveProduct(redSocks);
		TransactionTO freddiesTransaction = TransactionTO.builder().customerId(savedFreddie.getId())
				.date(Date.valueOf("2018-08-12")).build();
		TransactionTO savedFreddiesTransaction1 = transactionService.createTransaction(freddiesTransaction);
		// when
		Status newStatus = Status.DELETED;
		savedFreddiesTransaction1.setStatus(newStatus);
		TransactionTO updatedTransaction = transactionService.update(savedFreddiesTransaction1);
		// then
		assertThat(updatedTransaction.getId()).isEqualTo(savedFreddiesTransaction1.getId());
		assertThat(transactionService.findById(updatedTransaction.getId()).getStatus()).isEqualTo(newStatus);

	}

	@Test
	public void shouldFindAllTransactions() throws Exception {

		// given
		int previouslyAddedTransactions = transactionService.findAllTransactions().size();
		AddressTO freddiesAddress = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456")
				.city("Poznan").build();
		CustomerTO freddieMercury = CustomerTO.builder().firstName("Freddie").lastName("Mercury")
				.birthdate(Date.valueOf("1965-10-09")).addressTO(freddiesAddress).email("freddie.mercury@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedFreddie = customerService.saveCustomer(freddieMercury);
		Status currentStatus = Status.IN_DELIVERY;
		ProductTO blueSocks = ProductTO.builder().productName("Soft Socks").price(4.99).margin(2.5).weight(0.1).build();
		ProductTO redSocks = ProductTO.builder().productName("Soft Socks").price(3.99).margin(2.5).weight(0.1).build();
		ProductTO savedBlueSocks = productService.saveProduct(blueSocks);
		ProductTO savedRedSocks = productService.saveProduct(redSocks);
		TransactionTO freddiesTransaction = TransactionTO.builder().customerId(savedFreddie.getId())
				.date(Date.valueOf("2018-08-12")).build();
		TransactionTO savedFreddiesTransaction1 = transactionService.createTransaction(freddiesTransaction);
		TransactionTO savedFreddiesTransaction2 = transactionService.createTransaction(freddiesTransaction);
		TransactionTO blueSocksInTransaction = transactionService.addProductToTransaction(savedBlueSocks,
				savedFreddiesTransaction1);
		TransactionTO redSocksInTransaction = transactionService.addProductToTransaction(savedRedSocks,
				savedFreddiesTransaction1);
		// when
		List<TransactionTO> allTransactions = transactionService.findAllTransactions();
		// then
		assertThat(allTransactions.size()).isEqualTo(2 + previouslyAddedTransactions);

	}

	@Test
	public void shouldFindTransactionsByCustomerName() throws Exception {

		// given
		Date birthdate = java.sql.Date.valueOf("1950-11-01");

		AddressTO address = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456").city("Poznan")
				.build();
		CustomerTO tinaTurner = CustomerTO.builder().firstName("Tina").lastName("Turner").birthdate(birthdate)
				.addressTO(address).email("tina.turner@gmail.com").phoneNumber("876543234").build();
		CustomerTO savedTina = customerService.saveCustomer(tinaTurner);

		TransactionTO tinasTransaction = TransactionTO.builder().customerId(savedTina.getId()).status(Status.EXECUTED)
				.build();

		TransactionTO createdTinasTransaction = transactionService.createTransaction(tinasTransaction);
		TransactionTO createdTinasTransaction1 = transactionService.createTransaction(tinasTransaction);

		ProductTO blueSocks = ProductTO.builder().productName("Soft Socks").price(10.0).margin(2.5).weight(0.1).build();
		ProductTO microwave = ProductTO.builder().productName("Microwave").price(1000.0).margin(2.5).weight(0.1)
				.build();
		ProductTO fridge = ProductTO.builder().productName("Fridge").price(2000.0).margin(2.5).weight(0.1).build();
		ProductTO savedBlueSocks = productService.saveProduct(blueSocks);
		ProductTO savedMicrowave = productService.saveProduct(microwave);

		TransactionTO tinasProducts = transactionService.addProductToTransaction(savedBlueSocks,
				createdTinasTransaction);
		TransactionTO tinasProducts1 = transactionService.addProductToTransaction(savedMicrowave,
				createdTinasTransaction1);

		// when
		String lastName = "Turner";
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setCustomerName(lastName);

		List<TransactionTO> tinasTransactions = transactionService.findTransactionsBySearchCriteria(searchCriteria);

		// then
		assertThat(tinasTransactions.size()).isEqualTo(2);
		assertThat(customerService.findById(tinasTransactions.get(0).getCustomerId()).getLastName())
				.isEqualTo(lastName);

	}

	@Test
	public void shouldCalculateTotalCostOfTransactionsWithStatus() throws Exception {
		// given
		Date date1 = Date.valueOf("2018-08-12");

		AddressTO address = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456").city("Poznan")
				.build();
		CustomerTO tinaTurner = CustomerTO.builder().firstName("Tina").lastName("Turner")
				.birthdate(Date.valueOf("1969-10-09")).addressTO(address).email("tina.turner@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedTina = customerService.saveCustomer(tinaTurner);
		CustomerTO eddieMurphy = CustomerTO.builder().firstName("Eddie").lastName("Murphy")
				.birthdate(Date.valueOf("1969-10-09")).addressTO(address).email("tina.turner@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedEddie = customerService.saveCustomer(eddieMurphy);

		TransactionTO tinasTransaction = TransactionTO.builder().customerId(savedTina.getId()).date(date1)
				.status(Status.IN_PREPARATION).build();
		TransactionTO eddiesTransaction = TransactionTO.builder().customerId(savedEddie.getId()).date(date1)
				.status(Status.IN_PREPARATION).build();

		TransactionTO createdTinasTransaction = transactionService.createTransaction(tinasTransaction);
		TransactionTO createdTinasTransaction1 = transactionService.createTransaction(tinasTransaction);

		ProductTO blueSocks = ProductTO.builder().productName("Soft Socks").price(10.0).margin(2.5).weight(0.1).build();
		ProductTO microwave = ProductTO.builder().productName("Soft Socks").price(1000.0).margin(2.5).weight(0.1)
				.build();
		ProductTO savedBlueSocks = productService.saveProduct(blueSocks);
		ProductTO savedMicrowave = productService.saveProduct(microwave);

		TransactionTO tinasProducts = transactionService.addProductToTransaction(savedBlueSocks,
				createdTinasTransaction);
		TransactionTO tinasProducts1 = transactionService.addProductToTransaction(savedMicrowave,
				createdTinasTransaction1);
		// when
		Double result = transactionService.calculateTotalCostOfTransactionsWithStatus(Status.IN_PREPARATION);
		// then
		assertNotNull(result);
		assertThat(result).isEqualTo(1010.0);
	}

	@Test
	public void shouldCalculateProfitFromPeriod() throws Exception {
		// given
		Date date1 = Date.valueOf("2018-08-12");
		Date date2 = Date.valueOf("2018-06-12");
		Date date3 = Date.valueOf("2018-04-12");

		AddressTO address = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456").city("Poznan")
				.build();
		CustomerTO tinaTurner = CustomerTO.builder().firstName("Tina").lastName("Turner")
				.birthdate(Date.valueOf("1969-10-09")).addressTO(address).email("tina.turner@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedTina = customerService.saveCustomer(tinaTurner);
		CustomerTO eddieMurphy = CustomerTO.builder().firstName("Eddie").lastName("Murphy")
				.birthdate(Date.valueOf("1969-10-09")).addressTO(address).email("tina.turner@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedEddie = customerService.saveCustomer(eddieMurphy);

		TransactionTO tinasTransaction = TransactionTO.builder().customerId(savedTina.getId()).date(date1)
				.status(Status.EXECUTED).build();
		TransactionTO eddiesTransaction = TransactionTO.builder().customerId(savedEddie.getId()).date(date2)
				.status(Status.EXECUTED).build();

		TransactionTO createdTinasTransaction = transactionService.createTransaction(tinasTransaction);
		TransactionTO createdEddiesTransaction1 = transactionService.createTransaction(eddiesTransaction);

		ProductTO blueSocks = ProductTO.builder().productName("Soft Socks").price(10.0).margin(2.5).weight(0.1).build();
		ProductTO microwave = ProductTO.builder().productName("Soft Socks").price(1000.0).margin(2.5).weight(0.1)
				.build();
		ProductTO savedBlueSocks = productService.saveProduct(blueSocks);
		ProductTO savedMicrowave = productService.saveProduct(microwave);

		TransactionTO tinasProducts = transactionService.addProductToTransaction(savedBlueSocks,
				createdTinasTransaction);
		TransactionTO eddiesProducts = transactionService.addProductToTransaction(savedMicrowave,
				createdEddiesTransaction1);
		// when
		Date beginDate1 = java.sql.Date.valueOf("2018-08-10");
		Date endDate1 = java.sql.Date.valueOf("2018-08-25");
		Date beginDate2 = java.sql.Date.valueOf("2018-06-10");
		Date endDate2 = java.sql.Date.valueOf("2018-06-25");
		Date beginDate3 =java.sql.Date.valueOf("2018-01-10");
		Date endDate3 =java.sql.Date.valueOf("2018-01-25");
		Double profit1 = transactionService.calculateProfitFromPeriod(beginDate1, endDate1);
		Double profit2 = transactionService.calculateProfitFromPeriod(beginDate2, endDate2);
		Double profit3 = transactionService.calculateProfitFromPeriod(beginDate3, endDate3);
		// then
		assertThat(profit1).isEqualTo(10D);
		assertThat(profit2).isEqualTo(1000D);

		assertThat(profit3).isNull();
	}

	@Test(expected = InvalidTransactionException.class)
	public void shouldThrowExceptionIfCostTooBig() throws InvalidTransactionException {
		//given
		AddressTO address = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456").city("Poznan")
				.build();
		CustomerTO tinaTurner = CustomerTO.builder().firstName("Tina").lastName("Turner")
				.birthdate(Date.valueOf("1969-10-09")).addressTO(address).email("tina.turner@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedTina = customerService.saveCustomer(tinaTurner);
		
		ProductTO microwave = ProductTO.builder().productName("Soft Socks").price(5100.0).margin(2.5).weight(0.1)
				.build();
		ProductTO savedMicrowave = productService.saveProduct(microwave);
		TransactionTO tinasTransaction = TransactionTO.builder().customerId(savedTina.getId()).date(Date.valueOf("2018-07-12"))
				.status(Status.EXECUTED).build();
		
		TransactionTO createdTinasTransaction = transactionService.createTransaction(tinasTransaction);
		//when 
		
		transactionService.addProductToTransaction(savedMicrowave, createdTinasTransaction);
		 
          
		
		
		
	}
	
}
