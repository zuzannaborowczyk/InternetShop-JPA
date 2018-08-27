package com.capgemini.mappers;

import com.capgemini.domain.Address;
import com.capgemini.types.AddressTO;

public class AddressMapper {

	public static AddressTO addressToTO(Address address) {
		if (address == null) {
			return null;
		}
		return AddressTO.builder().city(address.getCity()).street(address.getStreet()).postalCode(address.getPostalCode())
				.streetNumber(address.getStreetNumber()).build();
	}
	public static Address addressToEntity(AddressTO addressTO) {
		return Address.builder().city(addressTO.getCity()).street(addressTO.getStreet()).postalCode(addressTO.getPostalCode())
				.streetNumber(addressTO.getStreetNumber()).build();
	}
}
