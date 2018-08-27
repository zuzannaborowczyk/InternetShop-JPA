package com.capgemini.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.capgemini.dao.CustomerRepository;
import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.Status;
import com.capgemini.types.AddressTO;
import com.capgemini.types.CustomerTO;
import com.capgemini.types.ProductTO;
import com.capgemini.types.TransactionProductTO;
import com.capgemini.types.TransactionTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTest {

	@Autowired
	CustomerService customerService;

	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	ProductService	 productService;
	
	@Autowired
	TransactionService transactionService;
	
	@Test
	public void shouldAddCustomer() {

		// given
		AddressTO freddiesAddress = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456")
				.city("Poznan").build();
		CustomerTO freddieMercury = CustomerTO.builder().firstName("Freddie").lastName("Mercury")
				.birthdate(Date.valueOf("1965-10-09")).addressTO(freddiesAddress).email("freddie.mercury@gmail.com")
				.phoneNumber("876543234").build();
		// when
		CustomerTO savedFreddie = customerService.saveCustomer(freddieMercury);
		long searchedId = savedFreddie.getId();
		CustomerTO foundFreddie = customerService.findById(searchedId);

		// then
		assertEquals("Mercury", foundFreddie.getLastName());

	}

	@Test
	public void shouldDeleteCustomer() {

		// given
		int previouslyAddedCustomers = customerService.findAllCustomers().size();
		AddressTO freddiesAddress = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456")
				.city("Poznan").build();
		CustomerTO freddieMercury = CustomerTO.builder().firstName("Freddie").lastName("Mercury")
				.birthdate(Date.valueOf("1965-10-09")).addressTO(freddiesAddress).email("freddie.mercury@gmail.com")
				.phoneNumber("876543234").build();
		AddressTO tinasAddress = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456")
				.city("Poznan").build();
		CustomerTO tinaTurner = CustomerTO.builder().firstName("Tina").lastName("Turner")
				.birthdate(Date.valueOf("1969-10-09")).addressTO(tinasAddress).email("tina.turner@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedFreddie = customerService.saveCustomer(freddieMercury);
		CustomerTO savedTina = customerService.saveCustomer(tinaTurner);

		// when
		customerService.deleteCustomer(savedTina.getId());
		List<CustomerTO> allCustomers = customerService.findAllCustomers();
		// then
		assertEquals(1+previouslyAddedCustomers, allCustomers.size());
		//assertThat(allCustomers.size()).isEqualTo(1);
	}

	@Test
	public void shouldFindAllCustomers() {

		// given
		int previouslyAddedCustomers = customerService.findAllCustomers().size();
		AddressTO freddiesAddress = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456")
				.city("Poznan").build();
		CustomerTO freddieMercury = CustomerTO.builder().firstName("Freddie").lastName("Mercury")
				.birthdate(Date.valueOf("1965-10-09")).addressTO(freddiesAddress).email("freddie.mercury@gmail.com")
				.phoneNumber("876543234").build();
		AddressTO tinasAddress = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456")
				.city("Poznan").build();
		CustomerTO tinaTurner = CustomerTO.builder().firstName("Tina").lastName("Turner")
				.birthdate(Date.valueOf("1969-10-09")).addressTO(tinasAddress).email("tina.turner@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedFreddie = customerService.saveCustomer(freddieMercury);
		CustomerTO savedTina = customerService.saveCustomer(tinaTurner);
		// when
		List<CustomerTO> allCustomers2 = customerService.findAllCustomers();
		// then
		assertThat(allCustomers2.size()).isEqualTo(2+previouslyAddedCustomers);
	}

	@Test
	public void shouldFindCustomerById() {

		// given
		AddressTO freddiesAddress = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456")
				.city("Poznan").build();
		CustomerTO freddieMercury = CustomerTO.builder().firstName("Freddie").lastName("Mercury")
				.birthdate(Date.valueOf("1965-10-09")).addressTO(freddiesAddress).email("freddie.mercury@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedFreddie = customerService.saveCustomer(freddieMercury);
		// when
		CustomerTO foundFreddie = customerService.findById(savedFreddie.getId());
		// then
		assertThat(savedFreddie.getFirstName()).isEqualTo(foundFreddie.getFirstName());
	}

	@Test
	public void shouldUpdateCustomerEmail() {

		// given
		int previouslyAddedCustomers = customerService.findAllCustomers().size();
		AddressTO freddiesAddress = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456")
				.city("Poznan").build();
		CustomerTO freddieMercury = CustomerTO.builder().firstName("Freddie").lastName("Mercury")
				.birthdate(Date.valueOf("1965-10-09")).addressTO(freddiesAddress).email("freddie.mercury@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedFreddie = customerService.saveCustomer(freddieMercury);
		String newEmail = "f.mercury@onet.pl";
		// when
		savedFreddie.setEmail(newEmail);
		CustomerTO updatedFreddie = customerService.update(savedFreddie);
		List<CustomerTO> allCustomers = customerService.findAllCustomers();
		// then
		assertThat(updatedFreddie.getEmail()).isEqualTo(newEmail);
		assertEquals(updatedFreddie.getId(), savedFreddie.getId());
		assertThat(customerService.findById(updatedFreddie.getId()).getEmail()).isEqualTo(newEmail);
		assertThat(allCustomers.size()).isEqualTo(1+previouslyAddedCustomers);
	}

	@Test
	public void shouldHaveCreationAndUpdateDate() throws InterruptedException {

		// given
		AddressTO tinasAddress = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456")
				.city("Poznan").build();
		CustomerTO tinaTurner = CustomerTO.builder().firstName("Tina").lastName("Turner")
				.birthdate(Date.valueOf("1969-10-09")).addressTO(tinasAddress).email("tina.turner@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedTina = customerService.saveCustomer(tinaTurner);
		String newLastName = "Day";
		// when
		savedTina.setLastName(newLastName);
		TimeUnit.SECONDS.sleep(1);
		customerService.update(savedTina);
		CustomerEntity foundTina = customerRepository.findOne(savedTina.getId());

		// then
		assertThat(foundTina.getCreationDate()).isNotNull();
		assertThat(foundTina.getUpdateDate()).isNotNull();

	}

	@Test
	public void shouldTestVersions() {

		// given
		AddressTO tinasAddress = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456")
				.city("Poznan").build();
		CustomerTO tinaTurner = CustomerTO.builder().firstName("Tina").lastName("Turner")
				.birthdate(Date.valueOf("1969-10-09")).addressTO(tinasAddress).email("tina.turner@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedTina = customerService.saveCustomer(tinaTurner);
		String newLastName = "Day";
		// when
		savedTina.setLastName(newLastName);
		Long version1 = customerRepository.findOne(savedTina.getId()).getVersion();
		customerService.update(savedTina);
		Long version2 = customerRepository.findOne(savedTina.getId()).getVersion();

		// then
		assertThat(version1).isNotEqualTo(version2);

	}
	
	@Test
	public void shouldFindThreeCustomersWhoSpendTheMostInPeriod() throws Exception {
		//given
		
		Date date1 = Date.valueOf("2018-08-12");
		Date date2 = Date.valueOf("2018-08-20");
		Date date3 = Date.valueOf("2018-08-21");
		
		AddressTO address = AddressTO.builder().street("Matejki").streetNumber(5).postalCode("65-456")
				.city("Poznan").build();
		CustomerTO tinaTurner = CustomerTO.builder().firstName("Tina").lastName("Turner")
				.birthdate(Date.valueOf("1969-10-09")).addressTO(address).email("tina.turner@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedTina = customerService.saveCustomer(tinaTurner);
		CustomerTO eddieMurphy = CustomerTO.builder().firstName("Eddie").lastName("Murphy")
				.birthdate(Date.valueOf("1969-10-09")).addressTO(address).email("tina.turner@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedEddie = customerService.saveCustomer(eddieMurphy);
		CustomerTO dorisDay = CustomerTO.builder().firstName("Doris").lastName("Day")
				.birthdate(Date.valueOf("1969-10-09")).addressTO(address).email("tina.turner@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedDoris = customerService.saveCustomer(dorisDay);
		CustomerTO johnTravolta = CustomerTO.builder().firstName("John").lastName("Travolta")
				.birthdate(Date.valueOf("1969-10-09")).addressTO(address).email("tina.turner@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedJohn = customerService.saveCustomer(johnTravolta);
		CustomerTO andreaBocelli = CustomerTO.builder().firstName("Andrea").lastName("Bocelli")
				.birthdate(Date.valueOf("1969-10-09")).addressTO(address).email("tina.turner@gmail.com")
				.phoneNumber("876543234").build();
		CustomerTO savedAndrea = customerService.saveCustomer(andreaBocelli);
		
		TransactionTO tinasTransaction = TransactionTO.builder().customerId(savedTina.getId()).date(date1).status(Status.EXECUTED).build();
		TransactionTO eddiesTransaction = TransactionTO.builder().customerId(savedEddie.getId()).date(date2).status(Status.EXECUTED).build();
		TransactionTO dorisTransaction = TransactionTO.builder().customerId(savedDoris.getId()).date(date2).status(Status.EXECUTED).build();
		TransactionTO johnsTransaction = TransactionTO.builder().customerId(savedJohn.getId()).date(date3).status(Status.EXECUTED).build();
		TransactionTO andreasTransaction = TransactionTO.builder().customerId(savedAndrea.getId()).date(date3).status(Status.EXECUTED).build();
		TransactionTO createdTinasTransaction = transactionService.createTransaction(tinasTransaction);
		TransactionTO createdEddiesTransaction = transactionService.createTransaction(eddiesTransaction);
		TransactionTO createdDorisTransaction = transactionService.createTransaction(dorisTransaction);
		TransactionTO createdJohnsTransaction = transactionService.createTransaction(johnsTransaction);
		TransactionTO createdAndreasTransaction = transactionService.createTransaction(andreasTransaction);
		
		ProductTO blueSocks = ProductTO.builder().productName("Soft Socks").price(10.0).margin(2.5).weight(0.1).build();
		ProductTO microwave = ProductTO.builder().productName("Soft Socks").price(1000.0).margin(2.5).weight(0.1).build();
		ProductTO fridge = ProductTO.builder().productName("Soft Socks").price(2000.0).margin(2.5).weight(0.1).build();
		ProductTO savedBlueSocks = productService.saveProduct(blueSocks);
		ProductTO savedMicrowave = productService.saveProduct(microwave);
		ProductTO savedFridge = productService.saveProduct(fridge);
		
		TransactionTO tinasProducts = transactionService.addProductToTransaction(savedBlueSocks, createdTinasTransaction);
		TransactionTO eddiesProducts = transactionService.addProductToTransaction(savedFridge, createdEddiesTransaction);
		TransactionTO dorisProducts = transactionService.addProductToTransaction(savedMicrowave, createdDorisTransaction);
		TransactionTO johnsProducts = transactionService.addProductToTransaction(savedFridge, createdJohnsTransaction);
		TransactionTO andreasProducts = transactionService.addProductToTransaction(savedBlueSocks, createdAndreasTransaction);
		
		TransactionProductTO tinasOrder = TransactionProductTO.builder().numberOfBoughtItems(1).productId(savedBlueSocks.getId())
				.transactionId(createdTinasTransaction.getId()).build();
		TransactionProductTO eddiesOrder = TransactionProductTO.builder().numberOfBoughtItems(1).productId(savedFridge.getId())
				.transactionId(createdEddiesTransaction.getId()).build();
		TransactionProductTO dorisOrder = TransactionProductTO.builder().numberOfBoughtItems(2).productId(savedMicrowave.getId())
				.transactionId(createdDorisTransaction.getId()).build();
		TransactionProductTO johnsOrder = TransactionProductTO.builder().numberOfBoughtItems(1).productId(savedFridge.getId())
				.transactionId(createdJohnsTransaction.getId()).build();
		TransactionProductTO andreasOrder = TransactionProductTO.builder().numberOfBoughtItems(1).productId(savedBlueSocks.getId())
				.transactionId(createdAndreasTransaction.getId()).build();
		
		TransactionProductTO savedTinasOrder = transactionService.saveTransactionProduct(tinasOrder);
		TransactionProductTO savedEddiesOrder = transactionService.saveTransactionProduct(eddiesOrder);
		TransactionProductTO savedDorisOrder = transactionService.saveTransactionProduct(dorisOrder);
		TransactionProductTO savedJohnsOrder = transactionService.saveTransactionProduct(johnsOrder);
		TransactionProductTO savedAndresOrder = transactionService.saveTransactionProduct(andreasOrder);
		
		Date beginDate = Date.valueOf("2018-08-15");
		Date endDate = Date.valueOf("2018-08-25");
		
		//when
		List<CustomerTO> threeCustomers = customerService.findThreeCustomersWhoSpendTheMostInPeriod(beginDate, endDate);
		//then
		assertNotNull(threeCustomers);
		assertEquals(3, threeCustomers.size());
		assertTrue(threeCustomers.stream().anyMatch(c->c.getLastName().equals("Murphy")));
		assertTrue(threeCustomers.stream().anyMatch(c->c.getLastName().equals("Day")));
		assertTrue(threeCustomers.stream().noneMatch(c->c.getLastName().equals("Turner")));
		
		
		
	}
}
