package org.psoft.ecommerce.rest.view;

import org.psoft.ecommerce.data.model.Address;

public class AddressView {

	Address address;

	public static AddressView create(Address address){
		return new AddressView(address);
	}
	
	private AddressView(Address address){
		this.address = address;
	}
	
	public Long getId() {
		return address.getId();
	}

	public void setId(Long addressId) {
		address.setId(addressId);
	}

	public String getName() {
		return address.getName();
	}

	public void setName(String name) {
		address.setName(name);
	}

	public String getAddress1() {
		return address.getAddress1();
	}

	public void setAddress1(String address1) {
		address.setAddress1(address1);
	}

	public String getAddress2() {
		return address.getAddress2();
	}

	public void setAddress2(String address2) {
		address.setAddress2(address2);
	}

	public String getCity() {
		return address.getCity();
	}

	public void setCity(String city) {
		address.setCity(city);
	}

	public String getPostalCode() {
		return address.getPostalCode();
	}

	public void setPostalCode(String postalCode) {
		address.setPostalCode(postalCode);
	}

	public String getState() {
		return address.getState();
	}

	public void setState(String state) {
		address.setState(state);
	}

	public String getPhone() {
		return address.getPhone();
	}

	public void setPhone(String phone) {
		address.setPhone(phone);
	}

	public String getAltPhone() {
		return address.getAltPhone();
	}

	public void setAltPhone(String altPhone) {
		address.setAltPhone(altPhone);
	}

	public String getCompany() {
		return address.getCompany();
	}

	public void setCompany(String company) {
		address.setCompany(company);
	}
}
