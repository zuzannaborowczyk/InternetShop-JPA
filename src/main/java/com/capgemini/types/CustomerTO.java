package com.capgemini.types;

import java.util.Date;
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
public class CustomerTO {

	private Long id;
	
	private Long version;
	private String firstName;

	private String lastName;

	private String email;

	private String phoneNumber;

	private AddressTO addressTO;

	private Date birthdate;

	private List<Long> transactionIdList;
}
