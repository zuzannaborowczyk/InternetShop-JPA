package com.capgemini.types;

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
public class AddressTO {

	private String street;
	private int streetNumber;
	private String postalCode;
	private String city;
}
