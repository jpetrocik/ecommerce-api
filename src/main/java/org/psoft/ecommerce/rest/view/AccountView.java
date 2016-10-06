package org.psoft.ecommerce.rest.view;

import org.psoft.ecommerce.data.model.Account;
import org.psoft.ecommerce.data.model.Address;

public class AccountView {

	Account account;
	
	Address shippingAddress;
	
	Address billingAddress;
	
	public static AccountView create(Account account){
		return new AccountView(account);
	}
	
	private AccountView(Account account) {
		this.account = account;
		this.billingAddress = account.getBillingAddress();
		if (billingAddress == null) {
			billingAddress = new Address();
		}
		
		this.shippingAddress = account.getShippingAddress();
		if (shippingAddress == null) {
			shippingAddress = new Address();
		}

	}

	public Long getId() {
		return account.getId();
	}
	
	public String getAccountType() {
		return account.getAccountType().getName();
	}

	public String getEmail() {
		return account.getEmail();
	}

	public String getBillingAddress1() {
		return billingAddress.getAddress1();
	}

	public String getBillingAddress2() {
		return billingAddress.getAddress2();
	}

	public String getBillingAltPhone() {
		return billingAddress.getAltPhone();
	}

	public String getBillingCity() {
		return billingAddress.getCity();
	}

	public String getBillingCompany() {
		return billingAddress.getCompany();
	}

	public String getBillingName() {
		return billingAddress.getName();
	}

	public String getBillingPhone() {
		return billingAddress.getPhone();
	}

	public String getBillingPostalCode() {
		return billingAddress.getPostalCode();
	}

	public String getBillingState() {
		return billingAddress.getState();
	}

	public String getShippingAddress1() {
		return shippingAddress.getAddress1();
	}

	public String getShippingAddress2() {
		return shippingAddress.getAddress2();
	}

	public String getShippingAltPhone() {
		return shippingAddress.getAltPhone();
	}

	public String getShippingCity() {
		return shippingAddress.getCity();
	}

	public String getShippingCompany() {
		return shippingAddress.getCompany();
	}

	public String getShippingName() {
		return shippingAddress.getName();
	}

	public String getShippingPhone() {
		return shippingAddress.getPhone();
	}

	public String getShippingPostalCode() {
		return shippingAddress.getPostalCode();
	}

	public String getShippingState() {
		return shippingAddress.getState();
	}

}
